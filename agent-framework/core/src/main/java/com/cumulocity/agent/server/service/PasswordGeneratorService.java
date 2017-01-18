package com.cumulocity.agent.server.service;

public interface PasswordGeneratorService {

    /**
     * Method to return an application specific password,
     * which will not change for the application.
     * 
     * @return A password useable for encryption.
     */
    public String applicationPassword();
}
