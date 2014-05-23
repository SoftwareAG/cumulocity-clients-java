package com.cumulocity.sdk.client;

import com.cumulocity.sdk.client.buffering.FileBasedPersistentProvider;
import com.cumulocity.sdk.client.buffering.PersistentProvider;

public class ClientConfiguration {

    private PersistentProvider persistentProvider;

    public ClientConfiguration() {
        this.persistentProvider = new FileBasedPersistentProvider();
    }
    
    public ClientConfiguration(PersistentProvider persistentProvider) {
        this.persistentProvider = persistentProvider;
    }

    public PersistentProvider getPersistentProvider() {
        return persistentProvider;
    }

}
