package com.cumulocity.microservice;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "microservice-deployable-package", defaultPhase = PACKAGE, requiresDependencyResolution = RUNTIME, threadSafe = true)
public class MicroservicePackageMojo extends AbstractMojo {

    @Parameter(defaultValue = "false", property = "skip.microservice.package")
    protected boolean skip;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("skipping agent packaging");
            return;
        }
    }

}
