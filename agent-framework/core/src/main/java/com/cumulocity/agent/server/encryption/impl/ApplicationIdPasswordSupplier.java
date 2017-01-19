package com.cumulocity.agent.server.encryption.impl;

import java.nio.charset.Charset;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.encryption.PasswordSupplier;
import com.google.common.base.Charsets;

@Component
public class ApplicationIdPasswordSupplier implements PasswordSupplier, InitializingBean {

    private static final Charset CHARSET = Charsets.UTF_8;

    @Value("${encryption.salt}")
    private String salt;

    @Value("${application.id}")
    private String applicationName;

    private String password;

    @Override
    public String get() {
        return password;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String plain = salt + applicationName;
        password = new String(Base64.encode(plain.getBytes(CHARSET)), CHARSET);
    }
}
