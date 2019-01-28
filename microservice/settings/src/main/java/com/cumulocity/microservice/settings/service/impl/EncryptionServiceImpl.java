package com.cumulocity.microservice.settings.service.impl;

import com.cumulocity.microservice.settings.service.EncryptionService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class EncryptionServiceImpl implements EncryptionService {

    private final TextEncryptor textEncryptor;

    public EncryptionServiceImpl(String encryptorPassword, String encryptorSalt) {
        this.textEncryptor = Encryptors.text(encryptorPassword, encryptorSalt);
    }

    @Override
    public String encrypt(String raw) {
        return textEncryptor.encrypt(raw);
    }

    @Override
    public String decrypt(String encrypted) {
        return textEncryptor.decrypt(encrypted);
    }
}
