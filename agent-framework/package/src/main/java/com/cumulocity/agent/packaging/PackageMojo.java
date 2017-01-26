package com.cumulocity.agent.packaging;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.io.Files.asByteSink;
import static com.google.common.io.Resources.asByteSource;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Calendar.YEAR;
import static org.apache.maven.shared.utils.io.FileUtils.cleanDirectory;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;
import org.twdata.maven.mojoexecutor.MojoExecutor.*;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;

@Mojo(name = "agent-package", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME,
      threadSafe = true)
public class PackageMojo extends AbstractMojo {

    @Component
    private BuildPluginManager pluginManager;

    @Component
    private MavenResourcesFiltering mavenResourcesFiltering;

    @Parameter(property = "package.name", defaultValue = "${project.artifactId}")
    private String name;

    @Parameter(property = "package.directory", defaultValue = "${project.artifactId}")
    private String directory;

    @Parameter(property = "package.description", defaultValue = "${project.description}")
    private String description;

    @Parameter(property = "agent-package.jvmArgs")
    private List<String> jvmArgs;

    @Parameter(property = "agent-package.arguments")
    private List<String> arguments;

    @Parameter(property = "project.build.sourceEncoding", defaultValue = "utf8")
    private String encoding;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession mavenSession;

    @Parameter(defaultValue = "${project.build.directory}")
    private File build;

    @Parameter(defaultValue = "${project.build.directory}/rpm-tmp")
    private File rpmTemporaryDirectory;

    @Parameter(defaultValue = "${project.build.directory}/docker-work")
    private File dockerWorkarea;

    @Parameter(defaultValue = "${project.build.directory}/rpm-base-build")
    private File workarea;

    @Parameter(defaultValue = "${basedir}/src/main/configuration")
    private File configurationDirectory;

    @Parameter(defaultValue = "${basedir}/src/main/docker")
    private File dockerDirectory;

    @Parameter(defaultValue = "false", property = "skip.agent.package")
    private boolean skip;

    @Parameter(defaultValue = "false", property = "skip.agent.package.rpm")
    private boolean rpmSkip;

    @Parameter(defaultValue = "true", property = "skip.agent.package.container")
    private boolean containerSkip;

    @Parameter(defaultValue = "${maven.compiler.target}")
    private String javaRuntime;

    @Parameter(property = "agent-package.heap")
    private Memory heap;

    @Parameter(property = "agent-package.perm")
    private Memory perm;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
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
        }
    }

    private void dockerContainer() throws MojoExecutionException {
        try {
            copyArtifact(new File(dockerWorkarea, "resources"));
            final File tmp = new File(build, "tmp");
            initializeTemplates(fromDirectory("docker"), tmp);
            filterResources(resource(tmp.getAbsolutePath()), dockerWorkarea);
            cleanDirectory(tmp);
            filterResources(resource(configurationDirectory.getAbsolutePath()), new File(dockerWorkarea, "etc"));
            filterResources(resource(dockerDirectory.getAbsolutePath()), dockerWorkarea);
        } catch (Exception e) {
            throw propagate(e);
        }
        Plugin plugin = plugin(groupId("com.spotify"), artifactId("docker-maven-plugin"), version("0.4.13"));

        //@formatter:off
        executeMojo(
            plugin,
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

    private void rpmPackage() throws MojoExecutionException {
        try {
            copyArtifact(new File(workarea, "bin"));
            initializeTemplates(fromDirectory("rpm"), rpmTemporaryDirectory);
            filterResources(resource(rpmTemporaryDirectory.getAbsolutePath()), workarea);
            filterResources(resource(configurationDirectory.getAbsolutePath()), new File(workarea, "etc"));
        } catch (Exception e) {
            throw propagate(e);
        }
        Plugin rpmPlugin = plugin(groupId("org.codehaus.mojo"), artifactId("rpm-maven-plugin"), version("2.1.5"));
        //@formatter:off
        executeMojo(
            rpmPlugin,
            goal("attached-rpm"),
            configuration(
                element(name("name"), name),
                element(name("group"), "C8Y/Agent"),
                element(name("distribution"), "Cumulocity GmbH " + currentYear()),
                element(name("packager"), "Cumulocity GmbH"),
                element(name("requires"), element(name("require"), "java >= " + javaRuntime)),
                element(name("repackJars"), String.valueOf(false)),
                mappings(
                    mapping(directory("/usr/lib/" + directory),
                        sources(source(location(new File(workarea, "bin").getAbsolutePath())))),
                    mapping(directory("/usr/lib/systemd/system"),
                        directoryIncluded(false),
                        isConfiguration(true),
                        sources(source(location(new File(workarea, "systemd").getAbsolutePath()),
                            includes(include(String.format("%s.service", name)))))),
                    mapping(directory("/etc/init.d"),
                        directoryIncluded(false),
                        isConfiguration(true),
                        sources(source(location(new File(workarea, "init.d").getAbsolutePath()),
                            includes(include(name))))),
                    mapping(directory("/etc/" + directory), directoryIncluded(false), isConfiguration(true),
                        sources(source(location(new File(workarea, "etc").getAbsolutePath())))))

            ), executionEnvironment(this.project, this.mavenSession, this.pluginManager));
        //@formatter:on
    }

    private String currentYear() {
        return String.valueOf(Calendar.getInstance().get(YEAR));
    }

    private void filterResources(Resource resource, File destination) throws Exception {
        final MavenResourcesExecution execution = new MavenResourcesExecution(ImmutableList.of(resource), destination, project, encoding,
                                                                                 ImmutableList.<String>of(), ImmutableList.<String>of(),
                                                                                 mavenSession);
        getLog().info("copy resources from " + resource + " to" + destination);
        createDirectories(destination.toPath());
        execution.setOverwrite(true);
        execution.setFilterFilenames(true);
        execution.setDelimiters(Sets.newLinkedHashSet(ImmutableList.of("@*@")));
        execution.setSupportMultiLineFiltering(true);
        final Properties props = new Properties();
        props.put("package.name", name);
        props.put("package.directory", directory);
        props.put("package.description", firstNonNull(description, name + " Service"));
        props.put("package.jvm-mem", Joiner.on(' ').join(getJvmMem()));
        props.put("package.jvm-gc", Joiner.on(' ').join(getJvmGc()));
        props.put("package.arguments", Joiner.on(' ').join(arguments));
        props.put("package.required-java", javaRuntime);
        props.put("package.java-version", getJavaVersion());
        execution.setAdditionalProperties(props);
        mavenResourcesFiltering.filterResources(execution);
    }

    private List<String> getJvmMem() {
        Memory heap = this.heap.or(new Memory("128m", "384m"));
        Memory perm = this.perm.or(new Memory("64m", "128m"));

        return ImmutableList.of("-Xms" + heap.getMin(), "-Xmx" + heap.getMax(), "-XX:PermSize=" + perm.getMin(),
            "-XX:MaxPermSize=" + perm.getMax());
    }

    private List<String> getJvmGc() {
        if (jvmArgs == null || jvmArgs.isEmpty()) {
            return ImmutableList.<String>builder().add("-XX:+UseConcMarkSweepGC", "-XX:+CMSParallelRemarkEnabled",
                "-XX:+ScavengeBeforeFullGC", "-XX:+CMSScavengeBeforeRemark").build();
        } else
            return jvmArgs;
    }

    public void initializeTemplates(Predicate<? super ResourceInfo> filter, File dest) throws IOException {
        getLog().debug("initialize templates");
        for (ResourceInfo resource : loadTemplates(filter)) {
            final URL url = resource.url();
            getLog().debug("template found " + resource.getResourceName());
            final String path = new File(resource.getResourceName()).getPath();
            final File destination = new File(dest, path.substring(path.indexOf('/')));
            Files.createParentDirs(destination);
            asByteSource(url).copyTo(asByteSink(destination));
        }

    }

    private void copyArtifact(File destination) throws IOException {
        getLog().debug(project.getArtifact().getFile().toString());
        final File to = new File(destination, String.format("%s.jar", name));
        Files.createParentDirs(to);
        getLog().debug(String.format("copy artifact %s to %s ", project.getArtifact().getFile().getAbsolutePath(), to.getAbsolutePath()));
        java.nio.file.Files.copy(project.getArtifact().getFile().toPath(), to.toPath(), REPLACE_EXISTING);
    }

    private ImmutableList<ResourceInfo> loadTemplates(Predicate<? super ResourceInfo> filter) throws IOException {
        return FluentIterable.from(ClassPath.from(getClass().getClassLoader()).getResources()).filter(filter).toList();
    }

    private Predicate<ResourceInfo> fromDirectory(final String directory) {
        return new Predicate<ResourceInfo>() {
            public boolean apply(ResourceInfo input) {
                return input.getResourceName().startsWith(directory);
            }
        };
    }

    public Resource resource(String resourceDirectory) {
        return resource(resourceDirectory, ImmutableList.<String>of(), ImmutableList.<String>of());
    }

    public Resource resource(String resourceDirectory, List<String> includes, List<String> excludes) {
        Resource resource = new Resource();
        resource.setDirectory(resourceDirectory);
        resource.setFiltering(true);
        resource.setIncludes(includes);
        resource.setExcludes(excludes);
        return resource;
    }

    private Element sources(Element... elements) {
        return element(name("sources"), elements);
    }

    private Element includes(Element... elements) {
        return element(name("includes"), elements);
    }

    private Element include(String value) {
        return element(name("include"), value);
    }

    private Element source(Element... elements) {
        return element(name("source"), elements);
    }

    private Element location(String applicationDirectory) {
        return element(name("location"), applicationDirectory);
    }

    private Element directory(String directory) {
        return element(name("directory"), directory);
    }

    private Element directoryIncluded(boolean enabled) {
        return element(name("directoryIncluded"), String.valueOf(enabled));
    }

    private Element isConfiguration(boolean enabled) {
        return element(name("configuration"), String.valueOf(enabled));
    }

    private Element mappings(Element... elements) {
        return element(name("mappings"), elements);
    }

    private Element mapping(Element... elements) {
        return element(name("mapping"),
            ObjectArrays.concat(elements, new Element[] { element(name("username"), "root"), element(name("filemode"), "777") },
                Element.class));
    }

    private String getJavaVersion() {
        if (javaRuntime.startsWith("1.")) {
            return javaRuntime.substring(2);
        }
        return javaRuntime;
    }
}
