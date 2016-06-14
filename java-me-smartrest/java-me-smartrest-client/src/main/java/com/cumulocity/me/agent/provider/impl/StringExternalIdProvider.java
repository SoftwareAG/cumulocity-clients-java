package com.cumulocity.me.agent.provider.impl;

import com.cumulocity.me.agent.bootstrap.exception.BootstrapFailedException;
import com.cumulocity.me.agent.provider.ExternalIdProvider;

public class StringExternalIdProvider implements ExternalIdProvider{

    private final String externalId;
    private final String externalIdType;

    public StringExternalIdProvider(String externalId, String externalIdType) {
        this.externalId = externalId;
        this.externalIdType = externalIdType;
    }
    
    public String getExternalId() throws BootstrapFailedException {
        return externalId;
    }

    public String getExternalIdType() {
        return externalIdType;
    }
}
