package com.cumulocity.agent.packaging;

import static com.cumulocity.agent.packaging.DockerDsl.docker;
import static com.cumulocity.agent.packaging.RpmDsl.*;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Throwables.propagate;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME;
import static org.apache.maven.shared.utils.io.FileUtils.cleanDirectory;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "agent-package", defaultPhase = PACKAGE, requiresDependencyResolution = RUNTIME, threadSafe = true)
public class PackageMojo extends BaseAgentMojo {

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
}
