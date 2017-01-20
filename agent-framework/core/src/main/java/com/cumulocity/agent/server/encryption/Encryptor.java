package com.cumulocity.agent.server.encryption;

import com.cumulocity.agent.server.encryption.exception.DecryptFailedException;

/**
 * Wrapper for spring Encryptors to add / remove prefix {cipher}.
 * 
 * @author Jens Wildhagen
 */
public interface Encryptor {

    /** Prefix to mark encrypted text as chipher */
    public static final String CIPHER = "{cipher}";

    /**
     * Method to symetrically encrypt a plain test.
     * 
     * @param plain Some plain test.
     * @return A encrypted text prefixed with {@code CIPHER}.
     */
    String encrypt(String plain);

    /**
     * Method to decrypt a previously enrypted string
     * 
     * @param encrypted The encrypted string.
     * @return The decrypted plain text.
     * @throws DecryptFailedException Thrown if decryption is not possible.
     */
    String decrypt(String encrypted) throws DecryptFailedException;
}
