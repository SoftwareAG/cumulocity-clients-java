package com.cumulocity.me.agent.provider;

import com.cumulocity.me.agent.bootstrap.exception.BootstrapFailedException;

public interface ExternalIdProvider {
    public String getExternalId() throws BootstrapFailedException;
    
    public String getExternalIdType();
}
