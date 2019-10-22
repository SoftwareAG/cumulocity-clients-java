package com.cumulocity.agent.packaging;

import com.cumulocity.agent.packaging.microservice.MicroserviceDockerClient;
import com.cumulocity.model.application.MicroserviceManifest;
import com.cumulocity.model.application.microservice.DataSize;
import com.cumulocity.model.application.microservice.Resources;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Value;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;

import javax.validation.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.cumulocity.agent.packaging.DockerDsl.docker;
import static com.cumulocity.agent.packaging.RpmDsl.configuration;
import static com.cumulocity.agent.packaging.RpmDsl.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.io.Files.asByteSource;
import static java.nio.file.Files.createDirectories;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "package", defaultPhase = PACKAGE, requiresDependencyResolution = RUNTIME, threadSafe = false)
public class PackageMojo extends BaseMicroserviceMojo {

    public static final String TARGET_FILENAME_PATTERN = "%s-%s.zip";
    public static final DataSize MEMORY_MINIMAL_LIMIT = DataSize.parse("178Mi");

    @Component
    private MicroserviceDockerClient dockerClient;

    @Parameter(defaultValue = "${basedir}/src/main/configuration/cumulocity.json")
    private File manifestFile;

    @Parameter(property = "package.name", defaultValue = "${project.artifactId}")
    private String image;

    @Parameter(property = "project.build.sourceEncoding", defaultValue = "utf8")
    private String encoding;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("skipping agent packaging");
            return;
        }

        if (!rpmSkip) {
            getLog().info("rpm package " + project.getArtifactId());
            rpmPackage();
        }

        if (!containerSkip) {
            getLog().info("docker container " + project.getArtifactId());
            dockerContainer();

            if (!skipMicroservicePackage) {
                getLog().info("microservice zip container " + project.getArtifactId());
                microserviceZip();
            }
        }
    }

    private void rpmPackage() throws MojoExecutionException {
        try {
            copyArtifact(new File(rpmBaseBuildDir, "bin"));
            copyFromPluginSubdirectory("rpm", rpmTmpDir);
            copyFromProjectSubdirectoryAndReplacePlaceholders(resource(rpmTmpDir.getAbsolutePath()), rpmBaseBuildDir, false);
            copyFromProjectSubdirectoryAndReplacePlaceholders(resource(srcConfigurationDir.getAbsolutePath()), new File(rpmBaseBuildDir, "etc"), true);
        } catch (Exception e) {
            throw propagate(e);
        }
        //@formatter:off
        executeMojo(
                rpm(),
                goal("attached-rpm"),
                configuration(
                        element("name", name),
                        element("group", "C8Y/Agent"),
                        element("distribution", "Cumulocity GmbH " + currentYear()),
                        element("packager", "Cumulocity GmbH"),
                        element("requires", element(name("require"), "java >= " + javaRuntime)),
                        element("repackJars", String.valueOf(false)),
                        mappings(
                                mapping(directory("/usr/lib/" + directory),
                                        sources(source(location(new File(rpmBaseBuildDir, "bin").getAbsolutePath())))),
                                mapping(directory("/usr/lib/systemd/system"),
                                        directoryIncluded(false),
                                        configuration(true),
                                        sources(source(location(new File(rpmBaseBuildDir, "systemd").getAbsolutePath()),
                                                includes(include(String.format("%s.service", name)))))),
                                mapping(directory("/etc/init.d"),
                                        directoryIncluded(false),
                                        configuration(true),
                                        sources(source(location(new File(rpmBaseBuildDir, "init.d").getAbsolutePath()),
                                                includes(include(name))))),
                                mapping(directory("/etc/" + directory), directoryIncluded(false), configuration("noreplace"),
                                        sources(source(location(new File(rpmBaseBuildDir, "etc").getAbsolutePath())))))

                ), executionEnvironment(this.project, this.mavenSession, this.pluginManager));
        //@formatter:on
    }

    private void dockerContainer() throws MojoExecutionException {

        final File dockerWorkResources = new File(dockerWorkDir, "resources");
        try {
//            copy artifact to docker work directory
            cleanDirectory(dockerWorkResources);
            copyArtifact(dockerWorkResources);

//            copy content of plugin src/main/resources/docker folder to docker work directory replacing placeholders
            final File buildTmp = new File(build, "tmp");
            copyFromPluginSubdirectory("docker", buildTmp);
            copyFromProjectSubdirectoryAndReplacePlaceholders(resource(buildTmp.getAbsolutePath()), dockerWorkDir, false);
            cleanDirectory(buildTmp);

//            copy content of project src/main/configuration to docker work directory replacing placeholders
            copyFromProjectSubdirectoryAndReplacePlaceholders(resource(srcConfigurationDir.getAbsolutePath()), new File(dockerWorkDir, "etc"), true);

//            copy content of project src/main/docker to docker work directory replacing placeholders
            copyFromProjectSubdirectoryAndReplacePlaceholders(resource(srcDockerDir.getAbsolutePath()), dockerWorkDir, true);

            //@formatter:off
            executeMojo(
                    docker(),
                    goal("build"),
                    configuration(
                            element(name("imageName"), image),
                            element("imageTags",
                                    element("imageTag", project.getVersion()),
                                    element("imageTag", "latest")
                            ),
                            element("dockerDirectory", dockerWorkDir.getAbsolutePath())
                    ),
                    executionEnvironment(this.project, this.mavenSession, this.pluginManager));
            //@formatter:on

//            cleaning up directory created by spotify docker plugin
            cleanDirectory(new File(build, "docker"));
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    private void microserviceZip() {
        try {
            final String targetFilename = String.format(TARGET_FILENAME_PATTERN, name, project.getVersion());
            final File dockerImage = new File(build, "image.tar");
            createDirectories(build.toPath());
            dockerClient.saveDockerImage(String.format("%s:%s", image, project.getVersion()), dockerImage);

            try (final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(
                    new File(build, targetFilename)))) {
                final File file = filterResourceFile(manifestFile);
                validateManifest(file);
                addFileToZip(zipOutputStream, file, "cumulocity.json");
                addFileToZip(zipOutputStream, dockerImage, "image.tar");
            }

            dockerImage.delete();
        } catch (Exception e) {
            propagate(e);
        }
    }

    private void validateManifest(File file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), Charsets.UTF_8)) {
            final MicroserviceManifest manifest = MicroserviceManifest.from(reader);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            FluentIterable<ManifestConstraintViolation> violations = FluentIterable.from(validator.validate(manifest))
                    .transform(new Function<ConstraintViolation<MicroserviceManifest>, ManifestConstraintViolation>() {
                        @Override
                        public ManifestConstraintViolation apply(ConstraintViolation<MicroserviceManifest> input) {
                            return new ManifestConstraintViolation(input.getPropertyPath().toString(), input.getMessage());
                        }
                    });

            violations = violations.append(validateMemory(manifest));

            if (!violations.isEmpty()) {
                for (String line : manifestValidationFailedMessage(violations)) {
                    getLog().error(line);
                }
                throw new ValidationException("Microservice manifest is invalid");
            }
        }
    }

    private ImmutableList<ManifestConstraintViolation> validateMemory(MicroserviceManifest manifest) {
        final Resources resources = manifest.getResources();
        if (resources != null && resources.getMemoryLimit().isPresent()) {
            final DataSize memoryLimit = resources.getMemoryLimit().get();
            if (memoryLimit.compareTo(MEMORY_MINIMAL_LIMIT) < 0) {
                return ImmutableList.of(new ManifestConstraintViolation("resources.memory", "For java project memory needs to be at least " + MEMORY_MINIMAL_LIMIT));
            }
        }
        return ImmutableList.of();
    }

    private <T> Iterable<String> manifestValidationFailedMessage(Iterable<ManifestConstraintViolation> result) {
        final List<String> sb = Lists.newArrayList("Microservice manifest invalid:");
        for (final Iterator<ManifestConstraintViolation> it = result.iterator(); it.hasNext(); ) {
            final ManifestConstraintViolation violation = it.next();
            sb.add(violation.getPath() + " - " + violation.getMessage());
        }
        return sb;
    }

    private File filterResourceFile(File source) throws IOException, MavenFilteringException {
        final File filteredDir = new File(build, File.separator + "filtered-resources");
        final MavenResourcesExecution execution = new MavenResourcesExecution(
                ImmutableList.of(resource(source)),
                filteredDir,
                project,
                encoding,
                ImmutableList.<String>of(),
                ImmutableList.<String>of(),
                mavenSession);
        createDirectories(filteredDir.toPath());
        execution.setOverwrite(true);
        mavenResourcesFiltering.filterResources(execution);
        return new File(filteredDir, File.separator + source.getName());
    }

    private Resource resource(File file) {
        Resource resource = new Resource();
        resource.setDirectory(file.getParent());
        resource.setFiltering(true);
        resource.setIncludes(ImmutableList.of(file.getName()));
        resource.setExcludes(ImmutableList.<String>of());
        return resource;
    }

    private void addFileToZip(final ZipOutputStream zipOutputStream, final File file, String name) throws IOException {
        final ZipEntry ze = new ZipEntry(name);
        try {
            zipOutputStream.putNextEntry(ze);
            asByteSource(file).copyTo(zipOutputStream);
        } finally {
            zipOutputStream.closeEntry();
        }
    }
    @Value
    private static final class ManifestConstraintViolation {
        private final String path;
        private final String message;
    }
}
