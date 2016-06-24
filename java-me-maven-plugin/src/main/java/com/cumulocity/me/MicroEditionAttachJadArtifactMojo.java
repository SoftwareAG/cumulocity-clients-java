package com.cumulocity.me;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.buildhelper.Artifact;
import org.codehaus.mojo.buildhelper.AttachArtifactMojo;

import java.io.File;

@Mojo( name = "attach-jad", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true )
public class MicroEditionAttachJadArtifactMojo extends AttachArtifactMojo {

    @Parameter( required = false )
    private Artifact[] artifacts;

    @Parameter( property = "file", required = false, defaultValue = "${project.build.directory}/${dist.jad}")
    private File jad;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Artifact artifact = new Artifact();
        artifact.setFile(jad);
        artifact.setType("jad");

        final Artifact[] artifacts = new Artifact[]{artifact};

        ReflectionUtils.setField(AttachArtifactMojo.class, this, "artifacts", artifacts);

        super.execute();
    }
}
