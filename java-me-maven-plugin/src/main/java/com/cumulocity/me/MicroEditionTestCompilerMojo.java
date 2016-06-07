package com.cumulocity.me;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.compiler.CompilationFailureException;
import org.apache.maven.plugin.compiler.TestCompilerMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo( name = "testCompile", defaultPhase = LifecyclePhase.TEST_COMPILE, threadSafe = true, requiresDependencyResolution = ResolutionScope.TEST )
public class MicroEditionTestCompilerMojo extends TestCompilerMojo {

    @Parameter(defaultValue = "1.7", property = "test.javac.source")
    private String testJavacSource;

    @Parameter(defaultValue = "1.7", property = "test.javac.target")
    private String testJavacTarget;

    @Override
    public void execute() throws MojoExecutionException, CompilationFailureException {
        source = testJavacSource;
        target = testJavacTarget;
        super.execute();
    }
}
