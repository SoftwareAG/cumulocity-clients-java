package com.cumulocity.microservice.settings.service;

import com.cumulocity.microservice.settings.service.impl.EncryptionServiceImpl;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EncryptionServiceTest {

    static final String ENCRYPTOR_PASSWORD = "XeWueTe8uhzeXTA5";
    static final String ENCRYPTOR_SALT = "D8711F69A2896D3E";

    private EncryptionService encryptionService = new EncryptionServiceImpl(ENCRYPTOR_PASSWORD, ENCRYPTOR_SALT);

    private String passwordRaw = "password_1";
    private String passwordEncrypted = "aab2ec7b57b92b9cf7162d65b35b085941878d36dc6d8fc77629c5931f3f2749";

    @Test
    public void mustEncryptConfidentValueWithAes() {
        // given
        String confidentialRaw = passwordRaw;
        // when
        String encrypted = encryptionService.encrypt(confidentialRaw);
        // then
        assertThat(encrypted).isNotNull().isNotEqualTo(confidentialRaw);
    }

    @Test
    public void mustDecryptAesEncryptedValue() {
        // given
        String confidentialEncrypted = passwordEncrypted;
        // when
        String decrypted = encryptionService.decrypt(confidentialEncrypted);
        // then
        assertThat(decrypted).isEqualTo(passwordRaw);
    }

}
