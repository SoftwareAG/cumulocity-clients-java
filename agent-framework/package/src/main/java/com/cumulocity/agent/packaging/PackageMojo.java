package com.cumulocity.agent.packaging;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.io.Files.asByteSink;
import static com.google.common.io.Resources.asByteSource;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Calendar.YEAR;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;
import org.twdata.maven.mojoexecutor.MojoExecutor.*;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
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

    @Component
    private BuildPluginManager pluginManager;

    @Parameter(defaultValue = "${project.build.directory}/rpm-tmp")
    private File rpmTemporaryDirectory;

    @Parameter(defaultValue = "${project.build.directory}/rpm-base-build")
    private File workarea;

    @Parameter(defaultValue = "${basedir}/src/main/configuration")
    private File configurationDirectory;

    @Parameter(defaultValue = "false", property = "skip.agent.package")
    private boolean skip;

    @Parameter(defaultValue = "${maven.compiler.target}")
    private String javaRuntime;

    @Parameter(property = "agent-package.heap")
    private Memory heap;

    @Parameter(property = "agent-package.perm")
    private Memory perm;

    @Component
    private MavenResourcesFiltering mavenResourcesFiltering;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("skiping agent packaging");
            return;
        }
        generateResources();
        attachRpm();
    }

    private void generateResources() {
        try {
            copyBaseArtifact();
            initializeTemplates();
            evaluateTemplates();
            copyConfiguration();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        } catch (MavenFilteringException e) {
            throw Throwables.propagate(e);
        }
    }

    private void copyConfiguration() throws IOException, MavenFilteringException {
        final File etc = new File(workarea, "etc");
        createDirectories(etc.toPath());

        filterResources(ImmutableList.of(resource(configurationDirectory.getAbsolutePath())), etc);
    }

    private void attachRpm() throws MojoExecutionException {
        Plugin rpmPlugin = plugin(groupId("org.codehaus.mojo"), artifactId("rpm-maven-plugin"), version("2.1.5"));

        executeMojo(rpmPlugin, goal("attached-rpm"), configuration(element(name("name"), name), element(name("group"), "C8Y/Agent"),
            element(name("distribution"), "Cumulocity " + Calendar.getInstance().get(YEAR)), element(name("packager"), "Cumulocity GmbH"),
            element(name("requires"), element(name("require"), "java >= " + javaRuntime)),
            element(name("repackJars"), String.valueOf(false)), mappings(
                mapping(directory("/usr/lib/"+directory), sources(source(location(new File(workarea, "bin").getAbsolutePath())))),
                mapping(directory("/usr/lib/systemd/system"), directoryIncluded(false), isConfiguration(true), sources(
                    source(location(new File(workarea, "systemd").getAbsolutePath()),
                        includes(include(String.format("%s.service", name)))))),
                mapping(directory("/etc/init.d"), directoryIncluded(false), isConfiguration(true),
                    sources(source(location(new File(workarea, "init.d").getAbsolutePath()), includes(include(name))))),
                mapping(directory("/etc/"+directory), directoryIncluded(false), isConfiguration(true),
                    sources(source(location(new File(workarea, "etc").getAbsolutePath())))))

        ), executionEnvironment(this.project, this.mavenSession, this.pluginManager));
    }

    public void evaluateTemplates() throws MavenFilteringException, IOException {
        Files.createParentDirs(workarea);
        filterResources(ImmutableList.of(resource(rpmTemporaryDirectory.getAbsolutePath())), workarea);
    }

    private void filterResources(List<Resource> resources, File destination) throws MavenFilteringException {
        final MavenResourcesExecution execution = new MavenResourcesExecution(resources, destination, project, encoding,
                                                                                 ImmutableList.<String>of(), ImmutableList.<String>of(),
                                                                                 mavenSession);
        execution.setOverwrite(true);
        execution.setFilterFilenames(true);
        execution.setDelimiters(Sets.newLinkedHashSet(ImmutableList.of("@*@")));
        execution.setSupportMultiLineFiltering(true);
        final Properties props = new Properties();
        props.put("package.name", name);
        props.put("package.directory", directory);
        props.put("package.description", firstNonNull(description, name + " Service"));
        props.put("package.jvm-opts", Joiner.on(' ').join(getJvmArgs()));
        props.put("package.arguments", Joiner.on(' ').join(arguments));
        props.put("package.required-java", javaRuntime);
        execution.setAdditionalProperties(props);
        mavenResourcesFiltering.filterResources(execution);
    }

    private List<String> getJvmArgs() {
        if (jvmArgs == null || jvmArgs.isEmpty()) {
            Memory heap = this.heap.or(new Memory("128m", "384m"));
            Memory perm = this.perm.or(new Memory("64m", "128m"));

            return ImmutableList.<String>builder().add("-Xms" + heap.getMin(), "-Xmx" + heap.getMax(), "-XX:PermSize=" + perm.getMin(),
                "-XX:MaxPermSize=" + perm.getMax(), "-XX:+UseConcMarkSweepGC", "-XX:+CMSParallelRemarkEnabled", "-XX:+ScavengeBeforeFullGC",
                "-XX:+CMSScavengeBeforeRemark").build();
        } else
            return jvmArgs;
    }

    public void initializeTemplates() throws IOException {
        getLog().debug("initialize templates");
        for (ResourceInfo resource : loadTemplates()) {
            final URL url = resource.url();
            getLog().debug("template found " + resource.getResourceName());
            final File destination = new File(rpmTemporaryDirectory, new File(resource.getResourceName()).getPath().substring(4));
            Files.createParentDirs(destination);
            asByteSource(url).copyTo(asByteSink(destination));
        }

    }

    public void copyBaseArtifact() throws IOException {
        getLog().debug(project.getArtifact().getFile().toString());
        final File destination = new File(workarea, "bin");
        final File to = new File(destination, String.format("%s.jar", name));
        Files.createParentDirs(to);
        getLog().debug(String.format("copy artifact %s to %s ", project.getArtifact().getFile().getAbsolutePath(), to.getAbsolutePath()));
        java.nio.file.Files.copy(project.getArtifact().getFile().toPath(), to.toPath(), REPLACE_EXISTING);
    }

    public ImmutableList<ResourceInfo> loadTemplates() throws IOException {
        return from(ClassPath.from(getClass().getClassLoader()).getResources()).filter(rpmResource()).toList();
    }

    public Predicate<ResourceInfo> rpmResource() {
        return new Predicate<ResourceInfo>() {
            public boolean apply(ResourceInfo input) {
                return input.getResourceName().startsWith("rpm");
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

    private Element[] includeFiles(File configurationDirectory) {
        final File[] files = configurationDirectory.listFiles();

        return FluentIterable.from(copyOf(files)).transform(new Function<File, Element>() {
            public Element apply(File input) {
                return include(input.getName());
            }

        }).toArray(Element.class);
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

    public static Element directoryIncluded(boolean enabled) {
        return element(name("directoryIncluded"), String.valueOf(enabled));
    }

    public static Element isConfiguration(boolean enabled) {
        return element(name("configuration"), String.valueOf(enabled));
    }

    public static Element mappings(Element... elements) {
        return element(name("mappings"), elements);
    }

    public static Element mapping(Element... elements) {
        return element(name("mapping"),
            ObjectArrays.concat(elements, new Element[] {
                element(name("username"), "root"),
                element(name("filemode"), "777") },
                Element.class));
    }

    public class CopyFileVisitor extends SimpleFileVisitor<Path> {
        private final Path targetPath;

        private Path sourcePath = null;

        public CopyFileVisitor(Path targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
            if (sourcePath == null) {
                sourcePath = dir;
            } else {
                java.nio.file.Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            getLog().info("copy " + file + " to " + targetPath.resolve(sourcePath.relativize(file)));
            java.nio.file.Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }
    }
}
