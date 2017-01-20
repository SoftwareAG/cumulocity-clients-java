package com.cumulocity.agent.server.encryption.impl;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.cumulocity.agent.server.encryption.Encryptor;
import com.cumulocity.agent.server.encryption.exception.DecryptFailedException;

public class DefaultEncryption implements Encryptor {

    private TextEncryptor encryptor;

    public static Encryptor encryption(String password, String salt) {
        return new DefaultEncryption(password, salt);
    }

    public static boolean isEncrypted(String encrypted) {
        if (encrypted.startsWith(CIPHER)) {
            return true;
        }
        return false;
    }

    public DefaultEncryption(String password, String salt) {
        this.encryptor = Encryptors.text(password, salt);
    }

    @Override
    public String decrypt(String encrypted) throws DecryptFailedException {
        if (!isEncrypted(encrypted)) {
            throw new DecryptFailedException("Cannot decrypt unencrypted string");
        }
        try {
            return encryptor.decrypt(encrypted.substring(CIPHER.length()));
        } catch (IllegalStateException e) {
            throw new DecryptFailedException("Decrypting of string failed", e);
        }
    }

    @Override
    public String encrypt(String plain) {
        return CIPHER + encryptor.encrypt(plain);
    }
}
