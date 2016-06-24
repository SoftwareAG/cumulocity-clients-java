package com.cumulocity.me;

import com.google.common.base.Throwables;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.properties.ReadPropertiesMojo;

import java.io.File;
import java.lang.reflect.Field;

@Mojo( name = "read-project-properties", defaultPhase = LifecyclePhase.NONE, threadSafe = true )
public class MicroEditionInitializeMojo extends ReadPropertiesMojo {

    @Parameter(defaultValue = "project.properties")
    private String properties;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        setFiles(new File[]{new File(getProject().getBasedir(), properties)});
        super.execute();
    }

    public MavenProject getProject() {
        try {
            final Field projectFile = ReadPropertiesMojo.class.getDeclaredField("project");
            projectFile.setAccessible(true);
            return (MavenProject) projectFile.get(this);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
