package com.cumulocity.me;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.dependency.fromDependencies.UnpackDependenciesMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME_PLUS_SYSTEM;

@Mojo( name = "unpack-dependencies", requiresDependencyResolution = RUNTIME_PLUS_SYSTEM, defaultPhase = PACKAGE, threadSafe = true )
public class MicroEditionUnpackDependenciesMojo extends UnpackDependenciesMojo {

    @Parameter(defaultValue = "**/*.class")
    private String includeFiles;

    @Parameter(defaultValue = "**/*.properties")
    private String excludeFiles;

    @Parameter(defaultValue = "${project.build.directory}/classes")
    private File output;

    @Override
    protected void doExecute() throws MojoExecutionException {
        setIncludes(includeFiles);
        setExcludes(excludeFiles);
        setOutputDirectory(output);
        super.doExecute();
    }
}
