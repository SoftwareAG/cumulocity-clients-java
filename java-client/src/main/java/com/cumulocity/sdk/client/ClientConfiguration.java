package com.cumulocity.sdk.client;

import com.cumulocity.sdk.client.buffering.BufferingConfiguration;

public class ClientConfiguration {
    
    private final BufferingConfiguration bufferingConfiguration;

    public ClientConfiguration(BufferingConfiguration configuration) {
        this.bufferingConfiguration = configuration;
    }

    public BufferingConfiguration getBufferingConfiguration() {
        return bufferingConfiguration;
    }
}
