package com.cumulocity.agent.server.context;


public interface DeviceCredentailsResolver<T> {

    boolean supports(Object credentialSource);

    DeviceCredentials get(T input);

}
