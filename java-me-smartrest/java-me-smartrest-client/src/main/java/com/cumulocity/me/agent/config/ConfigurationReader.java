package com.cumulocity.me.agent.config;

import java.io.IOException;

public interface ConfigurationReader {
    public Configuration read() throws IOException;
}
