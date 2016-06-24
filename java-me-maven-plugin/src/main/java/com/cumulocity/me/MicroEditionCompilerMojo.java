package com.cumulocity.me;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.compiler.CompilationFailureException;
import org.apache.maven.plugin.compiler.CompilerMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.util.TreeMap;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class MicroEditionCompilerMojo extends CompilerMojo {

    @Parameter(defaultValue = "1.3", property = "javac.source")
    private String javacSource;

    @Parameter(defaultValue = "1.1", property = "javac.target")
    private String javacTarget;

    @Parameter
    private String bootclasspath;

    @Override
    public void execute() throws CompilationFailureException, MojoExecutionException {
        source = javacSource;
        target = javacTarget;
        if (bootclasspath != null && bootclasspath.length() > 0) {
            if (compilerArguments == null) {
                compilerArguments = new TreeMap<>();
            }
            compilerArguments.put("bootclasspath", bootclasspath);
        }
        super.execute();
    }

    @Override
    protected String getSource() {
        return javacSource;
    }

    @Override
    protected String getTarget() {
        return javacTarget;
    }
}
