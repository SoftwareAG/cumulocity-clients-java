package com.cumulocity.me.agent.config;

import java.io.IOException;

import com.cumulocity.me.agent.config.model.Configuration;

public interface ConfigurationReader {
    public Configuration read() throws IOException;
}
