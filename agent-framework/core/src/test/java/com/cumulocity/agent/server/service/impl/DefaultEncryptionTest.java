package com.cumulocity.agent.server.service.impl;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.agent.server.encryption.Encryption;
import com.cumulocity.agent.server.encryption.exception.DecryptFailedException;
import com.cumulocity.agent.server.encryption.impl.DefaultEncryption;

public class DefaultEncryptionTest {

    private static final String PLAIN_TEXT = "secret message";

    @Test
    public void shouldEncryptDecryptSame() throws Exception {
        //given
        Encryption encryptionService = new DefaultEncryption("password");

        //when
        String encrypted = encryptionService.encrypt(PLAIN_TEXT);
        String decrypted = encryptionService.decrypt(encrypted);

        //then
        assertThat(encrypted).isNotEqualTo(PLAIN_TEXT);
        assertThat(decrypted).isEqualTo(PLAIN_TEXT);
    }

    @Test(expected = DecryptFailedException.class)
    public void shouldDependOnApplicationName() throws Exception {
        //given
        Encryption encryptionService = new DefaultEncryption("password");
        Encryption decryptionService = new DefaultEncryption("wrong password");

        //when
        String encrypted = encryptionService.encrypt(PLAIN_TEXT);
        decryptionService.decrypt(encrypted);

        //then
        //expect DecryptFailedException
    }

    @Test
    public void shouldStartWithPrefix() throws Exception {
        //given
        Encryption encryptionService = new DefaultEncryption("password");

        //when
        String encrypted = encryptionService.encrypt(PLAIN_TEXT);

        //then
        assertThat(encrypted).startsWith(Encryption.CIPHER);
    }

    @Test(expected = DecryptFailedException.class)
    public void shouldFailWhenNotEncrypted() throws Exception {
        //given
        Encryption encryptionService = new DefaultEncryption("password");

        //when
        encryptionService.decrypt(PLAIN_TEXT);

        //then
        //expect DecryptFailedException
    }

}