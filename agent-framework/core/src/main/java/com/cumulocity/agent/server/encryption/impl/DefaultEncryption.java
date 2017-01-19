package com.cumulocity.agent.server.encryption.impl;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import com.cumulocity.agent.server.encryption.Encryption;
import com.cumulocity.agent.server.encryption.exception.DecryptFailedException;
import com.google.common.base.Charsets;

public class DefaultEncryption implements Encryption {

    private static final Charset CHARSET = Charsets.UTF_8;

    private static final StringKeyGenerator KEY_GENERATOR = KeyGenerators.string();

    private String password;

    public DefaultEncryption(String password) {
        this.password = password;
    }

    public static Encryption encryption(String password) {
        return new DefaultEncryption(password);
    }

    public static boolean isEncrypted(String encrypted) {
        if (encrypted.startsWith(CIPHER)) {
            return true;
        }
        return false;
    }

    @Override
    public String decrypt(String encrypted) throws DecryptFailedException {
        if (!isEncrypted(encrypted)) {
            throw new DecryptFailedException("Cannot decrypt unencrypted string");
        }
        String withoutPrefix = withoutPrefix(encrypted);
        String salt = extractSalt(withoutPrefix);
        String text = extractKey(withoutPrefix);
        try {
            return Encryptors.text(password, salt).decrypt(text);
        } catch (IllegalStateException e) {
            throw new DecryptFailedException("Decrypting of string failed", e);
        }
    }

    @Override
    public String encrypt(String plain) {
        String salt = KEY_GENERATOR.generateKey();
        String hash = Encryptors.text(password, salt).encrypt(plain);
        return CIPHER + mergeSalt(hash, salt);
    }

    private String mergeSalt(String encrypted, String salt) {
        //!TODO: Check if it's safe to store the salt with the encrypted text
        String merged = encrypted + salt;
        return new String(Base64.encode(merged.getBytes(CHARSET)), CHARSET);
    }

    private String extractSalt(String encrypted) {
        String decoded = new String(Base64.decode(encrypted.getBytes(CHARSET)));
        return StringUtils.right(decoded, 16);
    }

    private String extractKey(String encrypted) {
        String decoded = new String(Base64.decode(encrypted.getBytes(CHARSET)));
        return decoded.substring(0, decoded.length() - 16);
    }

    private String withoutPrefix(String prefixedString) {
        return prefixedString.substring(CIPHER.length());
    }
}
