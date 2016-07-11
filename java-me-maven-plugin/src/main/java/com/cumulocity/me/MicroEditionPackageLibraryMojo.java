package com.cumulocity.me;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.assembly.mojos.SingleAssemblyMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo( name = "package-library", inheritByDefault = false, requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true )
public class MicroEditionPackageLibraryMojo extends SingleAssemblyMojo {

    public static final String MICRO_EDITION = "micro-edition-library";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        setDescriptorRefs(new String[]{MICRO_EDITION});
        final MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
        setArchive(archive);
        setAppendAssemblyId(false);
        super.execute();
    }
}
