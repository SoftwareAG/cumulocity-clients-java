package com.cumulocity.me.common;

import com.cumulocity.me.ReflectionUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.install.InstallFileMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class MicroEditionInstallMojo extends InstallFileMojo {

    @Parameter( defaultValue = "${project}", readonly = true, required = true )
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ReflectionUtils.setField(InstallFileMojo.class, this, "file", getFile());
        ReflectionUtils.setField(InstallFileMojo.class, this, "groupId", project.getGroupId());
        ReflectionUtils.setField(InstallFileMojo.class, this, "artifactId", project.getArtifactId());
        ReflectionUtils.setField(InstallFileMojo.class, this, "packaging", getPackaging());
        ReflectionUtils.setField(InstallFileMojo.class, this, "version", project.getVersion());

        super.execute();
    }

    protected abstract Object getPackaging();

    protected abstract Object getFile();
}
