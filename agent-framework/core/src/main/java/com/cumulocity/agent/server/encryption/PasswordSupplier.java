package com.cumulocity.agent.server.encryption;

import com.google.common.base.Supplier;

public interface PasswordSupplier extends Supplier<String> {

    /**
     * Method to return an application specific password,
     * which will not change for the application.
     * 
     * @return A password useable for encryption.
     */
    public String get();
}
