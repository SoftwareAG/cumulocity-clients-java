package com.cumulocity.me;

import com.cumulocity.me.common.MicroEditionInstallMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "install-file", requiresProject = false, aggregator = true, threadSafe = true)
public class MicroEditionInstallJadMojo extends MicroEditionInstallMojo {

    @Parameter( property = "file", required = false, defaultValue = "${project.build.directory}/${dist.jad}")
    private File file;

    public File getFile() {
        return file;
    }

    public String getPackaging() {
        return "jad";
    }
}
