package com.cumulocity.me.agent.provider;

public interface MidletInformationProvider {
    public String getVersion();
    public String getName();
    public String getUrl();

    public String get(String key);
}
