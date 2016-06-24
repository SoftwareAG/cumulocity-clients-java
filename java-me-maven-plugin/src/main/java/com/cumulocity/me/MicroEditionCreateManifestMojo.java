package com.cumulocity.me;

import com.cumulocity.me.common.MicroEditionAntRunMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo( name = "create-jad", threadSafe = true, requiresDependencyResolution = ResolutionScope.NONE )
public class MicroEditionCreateManifestMojo extends MicroEditionAntRunMojo {
    @Override
    public void beforeExecute() {
        addTarget("init");
        addTarget("create-jad");
        addTarget("set-up-build-classes-dir");
        addTarget("pre-preverify");
        addTarget("do-preverify");
        addTarget("post-preverify");
    }
}
