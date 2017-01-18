package com.cumulocity.agent.server.service.impl;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.agent.server.service.StringEncryptionService;
import com.cumulocity.agent.server.service.exception.DecryptFailedException;

public class ValueEncryptionServiceImplTest {

    private StringEncryptionService encryptionService;

    @Before
    public void setUp() throws Exception {
        encryptionService = new StringEncryptionServiceImpl();
    }

    @Test
    public void shouldEncryptDecryptSame() throws Exception {
        //given
        String password = "secret";

        //when
        String encrypted = encryptionService.encryptString(password, password);
        String decrypted = encryptionService.decryptString(encrypted, password);

        //then
        assertThat(encrypted).isNotEqualTo(password);
        assertThat(decrypted).isEqualTo(password);
    }

    @Test(expected = DecryptFailedException.class)
    public void shouldDependOnApplicationName() throws Exception {
        //given
        String password = "secure";
        String wrongPassword = "wrongPassword";

        //when
        String encrypted = encryptionService.encryptString(password, password);
        encryptionService.decryptString(encrypted, wrongPassword);

        //then
        //expect DecryptFailedException
    }

    @Test
    public void shouldStartWithPrefix() throws Exception {
        //given
        String password = "secure";

        //when
        String encrypted = encryptionService.encryptString(password, password);

        //then
        assertThat(encrypted).startsWith(StringEncryptionService.CIPHER);
    }

    @Test(expected = DecryptFailedException.class)
    public void shouldFailWhenNotEncrypted() throws Exception {
        //given
        String notEncrypted = "doesNotStartWithPrefix";

        //when
        encryptionService.decryptString(notEncrypted, "somePassword");

        //then
        //expect DecryptFailedException
    }

}