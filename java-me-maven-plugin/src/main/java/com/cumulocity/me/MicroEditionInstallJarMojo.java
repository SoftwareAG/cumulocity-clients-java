package com.cumulocity.me;

import com.cumulocity.me.common.MicroEditionInstallMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo( name = "install", defaultPhase = LifecyclePhase.INSTALL, threadSafe = true )
public class MicroEditionInstallJarMojo extends MicroEditionInstallMojo {

    @Parameter( property = "file", required = false, defaultValue = "${project.build.directory}/${dist.jar}")
    private File file;

    public File getFile() {
        return file;
    }

    public String getPackaging() {
        return "jar";
    }
}
