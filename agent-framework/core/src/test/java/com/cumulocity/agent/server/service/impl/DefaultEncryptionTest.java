package com.cumulocity.agent.server.service.impl;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.agent.server.encryption.Encryptor;
import com.cumulocity.agent.server.encryption.exception.DecryptFailedException;
import com.cumulocity.agent.server.encryption.impl.DefaultEncryption;

public class DefaultEncryptionTest {

    private static final String PLAIN_TEXT = "secret message";

    private static final String PASSWORD = "password";

    private static final String SALT = "4d985faa91fd3694";

    @Test
    public void shouldEncryptDecryptSame() throws Exception {
        //given
        Encryptor encryptionService = DefaultEncryption.encryption(PASSWORD, SALT);

        //when
        String encrypted = encryptionService.encrypt(PLAIN_TEXT);
        String decrypted = encryptionService.decrypt(encrypted);

        //then
        assertThat(encrypted).isNotEqualTo(PLAIN_TEXT);
        assertThat(decrypted).isEqualTo(PLAIN_TEXT);
    }

    @Test(expected = DecryptFailedException.class)
    public void shouldNotDecryptWithWrongPassword() throws Exception {
        //given
        Encryptor encryptionService = DefaultEncryption.encryption(PASSWORD, SALT);
        Encryptor decryptionService = DefaultEncryption.encryption("wrong password", SALT);

        //when
        String encrypted = encryptionService.encrypt(PLAIN_TEXT);
        decryptionService.decrypt(encrypted);

        //then
        //expect DecryptFailedException
    }

    @Test
    public void shouldStartWithPrefix() throws Exception {
        //given
        Encryptor encryptionService = DefaultEncryption.encryption(PASSWORD, SALT);

        //when
        String encrypted = encryptionService.encrypt(PLAIN_TEXT);

        //then
        assertThat(encrypted).startsWith(Encryptor.CIPHER);
    }

    @Test(expected = DecryptFailedException.class)
    public void shouldFailWhenNotEncrypted() throws Exception {
        //given
        Encryptor encryptionService = DefaultEncryption.encryption(PASSWORD, SALT);

        //when
        encryptionService.decrypt(PLAIN_TEXT);

        //then
        //expect DecryptFailedException
    }

}