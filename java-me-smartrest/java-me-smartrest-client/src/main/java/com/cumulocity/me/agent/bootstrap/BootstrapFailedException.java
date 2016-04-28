package com.cumulocity.me.agent.bootstrap;

import com.cumulocity.me.agent.feature.FeatureInitializationException;

public class BootstrapFailedException extends FeatureInitializationException{

    public BootstrapFailedException(String s) {
        super(s);
    }
}
