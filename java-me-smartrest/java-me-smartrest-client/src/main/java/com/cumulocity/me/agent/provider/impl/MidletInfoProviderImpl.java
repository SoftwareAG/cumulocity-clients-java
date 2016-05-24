package com.cumulocity.me.agent.provider.impl;

import com.cumulocity.me.agent.provider.MidletInformationProvider;

import javax.microedition.midlet.MIDlet;

public class MidletInfoProviderImpl implements MidletInformationProvider {
    private static final String VERSION_APP_KEY = "MIDlet-Version";
    private static final String NAME_APP_KEY = "MIDlet-Name";
    private static final String URL_APP_KEY = "MIDlet-Jar-URL";

    private final MIDlet midlet;

    public MidletInfoProviderImpl(MIDlet midlet) {
        this.midlet = midlet;
    }

    public String getVersion() {
        return midlet.getAppProperty(VERSION_APP_KEY);
    }

    public String getName() {
        return midlet.getAppProperty(NAME_APP_KEY);
    }

    public String getUrl() {
        return midlet.getAppProperty(URL_APP_KEY);
    }

    public String get(String key) {
        return midlet.getAppProperty(key);
    }
}
