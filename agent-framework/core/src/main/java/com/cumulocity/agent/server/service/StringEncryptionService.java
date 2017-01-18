package com.cumulocity.agent.server.service;

import com.cumulocity.agent.server.service.exception.DecryptFailedException;

public interface StringEncryptionService {

    /** Prefix to mark encrypted text as chipher */
    public static final String CIPHER = "{cipher}";

    /**
     * Method to symetrically encrypt a plain test.
     * 
     * @param plain Some plain test.
     * @param password The password to be used for decryption.
     * @return A encrypted text prefixed with {@code CIPHER}.
     */
    String encryptString(String plain, String password);

    /**
     * Method to decrypt a previously enrypted string
     * 
     * @param encrypted The encrypted string.
     * @param password The password to be used for decryption.
     * @return The decrypted plain text.
     * @throws DecryptFailedException Thrown if decryption is not possible.
     */
    String decryptString(String encrypted, String password) throws DecryptFailedException;
}
