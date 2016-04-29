package com.cumulocity.me.agent.config;

import java.io.IOException;

import com.cumulocity.me.agent.config.model.Configuration;

public interface ConfigurationWriter {
    public void write(Configuration configuration) throws IOException;
}
