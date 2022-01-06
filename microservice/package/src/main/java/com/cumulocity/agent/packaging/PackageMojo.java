package com.cumulocity.agent.packaging;

import com.cumulocity.agent.packaging.microservice.MicroserviceDockerClient;
import com.cumulocity.model.application.MicroserviceManifest;
import com.cumulocity.model.application.microservice.DataSize;
import com.cumulocity.model.application.microservice.Resources;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.*;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Proxy;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;

import javax.validation.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.cumulocity.agent.packaging.RpmDsl.configuration;
import static com.cumulocity.agent.packaging.RpmDsl.*;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.io.Files.asByteSource;
import static java.nio.file.Files.createDirectories;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Slf4j
@Mojo(name = "package", defaultPhase = PACKAGE, requiresDependencyResolution = RUNTIME, threadSafe = false)
public class PackageMojo extends BaseMicroserviceMojo {

    public static final String TARGET_FILENAME_PATTERN = "%s-%s-%s.zip";
    public static final DataSize MEMORY_MINIMAL_LIMIT = DataSize.parse("178Mi");

    public static final String BUILD_SPEC_FRAGMENT="buildSpec";
    public static final String DOCKER_IMGARCH_BUILDARG ="imageArch";
    public static final String DOCKER_BUILD_INFO_FRAGMENT="dockerBuildInfo";
    public static final String MANIFEST_JSON_FILENAME = "cumulocity.json";
    public static final String IMAGE_FILENAME_PATTERN = "image-%s.tar";

    @Component
    private MicroserviceDockerClient dockerClient;

    @Parameter(defaultValue = "${basedir}/src/main/configuration/cumulocity.json")
    private File manifestFile;

    @Parameter(property = "package.name", defaultValue = "${project.artifactId}")
    private String image;

    @Parameter(defaultValue = "")
    private String dockerBuildNetwork;

    @Parameter(property= "microservice.package.deleteImage",defaultValue = "true")
    private Boolean deleteImage = true;

    @Parameter(property = "microservice.package.dockerBuildArchs")
    private String targetBuildArchs;

    @Parameter(defaultValue = "${mojoExecution}")
    protected MojoExecution mojoExecution;

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

            Iterable<String> buildTargetArchitectures = getTargetBuildArchitectures();

            log.info("Starting docker microservice build for the following target architectures: {}", Arrays.asList(buildTargetArchitectures));

            for (String arch: buildTargetArchitectures) {

                getLog().info("docker container " + project.getArtifactId() + " in network '" + (dockerBuildNetwork != null ? dockerBuildNetwork : "<default>") + "'");
                dockerContainer(arch);

                if (!skipMicroservicePackage) {
                    getLog().info("microservice zip container " + project.getArtifactId());
                    microserviceZip(arch);
                }
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

    private void dockerContainer(String targetArchitecture) throws MojoExecutionException {

        final File dockerWorkResources = new File(dockerWorkDir, "resources");
        try {
//          copy artifact to docker work directory
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

//          build docker image with target architecture
            buildDockerImage(targetArchitecture);

//          cleaning up directory created by spotify docker plugin
            cleanDirectory(new File(build, "docker"));
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SneakyThrows
    private Iterable<String> getTargetBuildArchitectures() {

        if (Objects.nonNull(targetBuildArchs)) {
            log.info("Using custom target build architectures from maven configuration (overriding microservice manifest): {}", targetBuildArchs);
            return Arrays.asList(targetBuildArchs.split(","));
        }

        final File file;

        try {
            file = filterResourceFile(manifestFile);
        } catch (IOException e) {
            log.error("Can't read manifest file {}", manifestFile);
            throw e;
        } catch (MavenFilteringException e) {
            throw e;
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> jsonManifest = mapper.readValue(file, Map.class);

        if (jsonManifest.containsKey(BUILD_SPEC_FRAGMENT)) {
            DockerBuildSpec buildSpec = mapper.convertValue(jsonManifest.get(BUILD_SPEC_FRAGMENT), DockerBuildSpec.class);
            log.info("Using build target images from build spec: {}", buildSpec.getTargetBuildArchitectures());
            return buildSpec.getTargetBuildArchitectures();
        } else {
            log.info("Manifest contains no buildspec and target architectures. Using defaults");
            return DockerBuildSpec.defaultBuildSpec().getTargetBuildArchitectures();
        }

        }




    private void buildDockerImage(String targetBuildArch) {
        log.info("Building microservice image for target architecture {}", targetBuildArch);
        Map<String, String> buildArgs = configureProxyBuildArguments();
        Set<String> tags = getDockerImageNameExpandedWithTags();

        if (targetBuildArch.equals(DockerBuildSpec.DEFAULT_TARGET_DOCKER_IMAGE_PLATFORM)) {
            log.warn("Your target build architecture {} does not match the default target {}", targetBuildArch, DockerBuildSpec.DEFAULT_TARGET_DOCKER_IMAGE_PLATFORM);
            log.warn("Currently, Cumulocity only supports hosting microservices build for {}", DockerBuildSpec.DEFAULT_TARGET_DOCKER_IMAGE_PLATFORM);
            log.warn("Your image might be incompatible with cloud hosting!");
        }

        //the target architecture is passed into the build arguments, because the dockerfile
        //contains an uppercase docker argument to choose different architectures
        //We also append a / to the buildarg to make the docker file work if it should be unset.
        buildArgs.put(DOCKER_IMGARCH_BUILDARG.toUpperCase(Locale.ROOT),targetBuildArch+"/");

        dockerClient.buildDockerImage(dockerWorkDir.getAbsolutePath(),tags,buildArgs, targetBuildArch, dockerBuildNetwork);


    }

    private DockerBuildInfo getDockerBuildInfo(String targetBuildArch) {;
        return DockerBuildInfo.defaultInfo()
                .withImageArch(targetBuildArch)
                .withCurrentBuildDate()
                .withBuilderInfo(mojoExecution.getPlugin().getId())
                .withHostArchitecture()
                .withHostOS();
    }

    private Set<String> getDockerImageNameExpandedWithTags() {
        String imageVersionTag = image+":"+project.getVersion();
        String imageLatestTag = image+":latest";

        Set<String> tags = Sets.newHashSet(imageLatestTag, imageVersionTag);
        return tags;
    }

    private Map<String, String> configureProxyBuildArguments() {
        String httpProxy = null;
        String httpsProxy = null;

        //loop about Proxy settings
        for (Proxy proxy : mavenSession.getSettings().getProxies() ) {
            if ( proxy.isActive() ) {
                String pS = proxy.getProtocol() + "://" + proxy.getHost() + ":" + proxy.getPort();
                getLog().info( "Found Proxy settings: " + pS );
                if ( proxy.getProtocol().equals( "http" ) )
                    httpProxy = pS;
                if ( proxy.getProtocol().equals( "https" ) )
                    httpsProxy = pS;
            }
        }

        if (httpsProxy == null && httpProxy != null )
            httpsProxy = httpProxy;

        if (httpProxy == null && httpsProxy != null )
            httpProxy = httpsProxy;

        Map<String, String> buildArgs = Maps.newHashMap();

        if (httpProxy != null ) {
            buildArgs.put("HTTP_PROXY", httpProxy);
            buildArgs.put("http_proxy", httpProxy);
        }

        if (httpsProxy != null ) {
            buildArgs.put("HTTPS_PROXY", httpsProxy);
            buildArgs.put("https_proxy", httpsProxy);
        }

        return buildArgs;
    }

    private void microserviceZip(String targetArchitecture) {
        try {

            String dockerImageFileName=String.format(IMAGE_FILENAME_PATTERN,targetArchitecture);
            final String targetFilename = String.format(TARGET_FILENAME_PATTERN, name, project.getVersion(), targetArchitecture);
            log.info("Saving microservice to {}", targetFilename);

            final File dockerImage = new File(build, dockerImageFileName);
            createDirectories(build.toPath());
            dockerClient.saveDockerImage(String.format("%s:%s", image, project.getVersion()), dockerImage);

            try (final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(
                    new File(build, targetFilename)))) {
                final File file = filterResourceFile(manifestFile);
                validateManifest(file);
                addPlatformTaggedMicroserviceManifestToZipFile(targetArchitecture, filterResourceFile(manifestFile), zipOutputStream);
                addFileToZip(zipOutputStream, dockerImage, "image.tar");
            }
            if(deleteImage!= null && deleteImage) {
                Set<String> imageNameWithTags = getDockerImageNameExpandedWithTags();
                log.info("Deleting all images named {} and imageNameWithTags {}", image);
                imageNameWithTags.forEach(imageNameWithTag -> {
                    dockerClient.deleteAll(imageNameWithTag, true);
                } );

            } else{
                getLog().info("Skipping docker image cleanup");
            }

            dockerImage.delete();
        } catch (Exception e) {
            propagate(e);
        }
    }

    private void addPlatformTaggedMicroserviceManifestToZipFile(String targetArchitecture, File manifestFile, ZipOutputStream zipOutputStream) throws IOException {

        log.info("Tagging microservice manifest with image platform");
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        ObjectNode manifest = (ObjectNode) mapper.readTree(manifestFile);

        DockerBuildInfo updated = getDockerBuildInfo(targetArchitecture);
        manifest.putPOJO(DOCKER_BUILD_INFO_FRAGMENT, updated);

        String jsonString = mapper.writeValueAsString(manifest);

        final ZipEntry ze = new ZipEntry(MANIFEST_JSON_FILENAME);
        zipOutputStream.putNextEntry(ze);

        BufferedWriter zipStringWriter = new BufferedWriter(new OutputStreamWriter(zipOutputStream));
        zipStringWriter.write(jsonString);
        zipStringWriter.flush();

        zipOutputStream.closeEntry();
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
            if (memoryLimit.compareTo(MEMORY_MINIMAL_LIMIT ) < 0) {
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
        log.info("Adding file {} to microservice zip", name);
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
