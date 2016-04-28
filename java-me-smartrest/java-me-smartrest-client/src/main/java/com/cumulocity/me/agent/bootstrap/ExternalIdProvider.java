package com.cumulocity.me.agent.bootstrap;

public interface ExternalIdProvider {
    public String getExternalId() throws BootstrapFailedException;
    
    public String getExternalIdType();
}
