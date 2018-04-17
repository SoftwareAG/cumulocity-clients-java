package com.cumulocity.agent.packaging;

import static com.cumulocity.agent.packaging.DockerDsl.docker;
import static com.cumulocity.agent.packaging.RpmDsl.*;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.io.Files.asByteSource;
import static java.nio.file.Files.createDirectories;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME;
import static org.apache.maven.shared.utils.io.FileUtils.cleanDirectory;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.cumulocity.agent.packaging.microservice.MicroserviceDockerClient;
import com.google.common.collect.ImmutableList;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;

@Mojo(name = "package", defaultPhase = PACKAGE, requiresDependencyResolution = RUNTIME, threadSafe = true)
public class PackageMojo extends BaseMicroserviceMojo {

    public static final String TARGET_FILENAME_PATTERN = "%s-%s.zip";

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
            copyArtifact(new File(workarea, "bin"));
            initializeTemplates(fromDirectory("rpm"), rpmTemporaryDirectory);
            filterResources(resource(rpmTemporaryDirectory.getAbsolutePath()), workarea,false);
            filterResources(resource(configurationDirectory.getAbsolutePath()), new File(workarea, "etc"),true);
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
                                        sources(source(location(new File(workarea, "bin").getAbsolutePath())))),
                                mapping(directory("/usr/lib/systemd/system"),
                                        directoryIncluded(false),
                                        configuration(true),
                                        sources(source(location(new File(workarea, "systemd").getAbsolutePath()),
                                                includes(include(String.format("%s.service", name)))))),
                                mapping(directory("/etc/init.d"),
                                        directoryIncluded(false),
                                        configuration(true),
                                        sources(source(location(new File(workarea, "init.d").getAbsolutePath()),
                                                includes(include(name))))),
                                mapping(directory("/etc/" + directory), directoryIncluded(false), configuration("noreplace"),
                                        sources(source(location(new File(workarea, "etc").getAbsolutePath())))))

                ), executionEnvironment(this.project, this.mavenSession, this.pluginManager));
        //@formatter:on
    }

    private void dockerContainer() throws MojoExecutionException {
        try {
            copyArtifact(new File(dockerWorkarea, "resources"));
            final File tmp = new File(build, "tmp");
            initializeTemplates(fromDirectory("docker"), tmp);
            filterResources(resource(tmp.getAbsolutePath()), dockerWorkarea,false);
            cleanDirectory(tmp);
            filterResources(resource(configurationDirectory.getAbsolutePath()), new File(dockerWorkarea, "etc"),true);
            filterResources(resource(dockerDirectory.getAbsolutePath()), dockerWorkarea,true);
        } catch (Exception e) {
            throw propagate(e);
        }

        //@formatter:off
        executeMojo(
            docker(),
            goal("build"),
            configuration(
                element(name("imageName"), name),
                element("imageTags",
                    element("imageTag",project.getVersion()),
                    element("imageTag","latest")
                ),
                element("dockerDirectory", dockerWorkarea.getAbsolutePath())
            ),
            executionEnvironment(this.project, this.mavenSession, this.pluginManager));
        //@formatter:on
    }

    private void microserviceZip() {
        try {
            final String targetFilename = String.format(TARGET_FILENAME_PATTERN, name, project.getVersion());
            final File dockerImage = new File(build, "image.tar");
            createDirectories(build.toPath());
            dockerClient.saveDockerImage(String.format("%s:%s",image,project.getVersion()), dockerImage);

            try (final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(
                    new File(build, targetFilename)))) {
                addFileToZip(zipOutputStream, filterResourceFile(manifestFile),"cumulocity.json");
                addFileToZip(zipOutputStream, dockerImage,"image.tar");
            }

            dockerImage.delete();
        } catch (Exception e) {
            propagate(e);
        }
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

    private void addFileToZip(final ZipOutputStream zipOutputStream, final File file,String name) throws IOException {
        final ZipEntry ze = new ZipEntry(name);
        try {
            zipOutputStream.putNextEntry(ze);
            asByteSource(file).copyTo(zipOutputStream);
        }finally {
            zipOutputStream.closeEntry();
        }
    }
}
