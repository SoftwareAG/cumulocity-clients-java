package com.cumulocity.me.agent.integration;

public interface DeviceInformationProvider {
   
    public String getModel();
    
    public String getSerialNumber();
    
    public String getRevision();
}
