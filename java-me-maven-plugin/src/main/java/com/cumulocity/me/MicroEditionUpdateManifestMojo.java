package com.cumulocity.me;

import com.cumulocity.me.common.MicroEditionAntRunMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo( name = "update-jad", threadSafe = true, requiresDependencyResolution = ResolutionScope.TEST )
public class MicroEditionUpdateManifestMojo extends MicroEditionAntRunMojo {
    @Override
    public void beforeExecute() {
        addTarget("init");
        addTarget("update-jad");
    }
}
