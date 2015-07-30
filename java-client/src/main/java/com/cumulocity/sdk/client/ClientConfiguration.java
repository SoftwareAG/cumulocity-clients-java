package com.cumulocity.sdk.client;

import com.cumulocity.sdk.client.buffering.MemoryBasedPersistentProvider;
import com.cumulocity.sdk.client.buffering.PersistentProvider;

public class ClientConfiguration {

    private final PersistentProvider persistentProvider;
    
    private final boolean asyncEnabled;

    public ClientConfiguration() {
        this(new MemoryBasedPersistentProvider(), true);
    }
    
    public ClientConfiguration(PersistentProvider persistentProvider) {
        this(persistentProvider, true);
    }
    
    public ClientConfiguration(PersistentProvider persistentProvider, boolean asyncEnabled) {
        this.asyncEnabled = asyncEnabled;
        this.persistentProvider = persistentProvider;
    }

    public PersistentProvider getPersistentProvider() {
        return persistentProvider;
    }

    public boolean isAsyncEnabled() {
        return asyncEnabled;
    }
}
