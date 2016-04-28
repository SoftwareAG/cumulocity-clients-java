package com.cumulocity.me.agent.config;

import java.io.IOException;

public interface ConfigurationWriter {
    public void write(Configuration configuration) throws IOException;
}
