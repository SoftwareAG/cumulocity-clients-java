package com.cumulocity.sdk.client.buffering;

public class DefaultBufferingConfiguration extends BufferingConfiguration {

    private static final int BUFFER_LIMIT = 10000;

    public DefaultBufferingConfiguration() {
        super(new FileBasedPersistentProvider("/tmp/"), BUFFER_LIMIT);
    }

}
