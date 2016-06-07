package com.cumulocity.me;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.assembly.mojos.SingleAssemblyMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;

@Mojo( name = "package", inheritByDefault = false, requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true )
public class MicroEditionPackageMojo extends SingleAssemblyMojo {

    public static final String MICRO_EDITION = "micro-edition";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        setDescriptorRefs(new String[]{MICRO_EDITION});
        final MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
        archive.setManifestFile(new File(getProject().getBuild().getDirectory(), "manifest.mf"));
        setArchive(archive);
        setAppendAssemblyId(false);
        super.execute();
    }
}
