package com.cumulocity.sdk.client.buffering;

public class BufferingConfiguration {

    private final PersistentProvider persistentProvider;
    private final long bufferLimit;

    public BufferingConfiguration(PersistentProvider persistentProvider, long bufferLimit) {
        this.persistentProvider = persistentProvider;
        this.bufferLimit = bufferLimit;
    }

    public PersistentProvider getPersistentProvider() {
        return persistentProvider;
    }

    public long getBufferLimit() {
        return bufferLimit;
    }
}
