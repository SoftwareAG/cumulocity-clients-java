package com.cumulocity.agent.server.service.impl;

import java.nio.charset.Charset;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.service.PasswordGeneratorService;
import com.google.common.base.Charsets;

@Component
public class ApplicationIdPasswordGeneratorService implements PasswordGeneratorService, InitializingBean {


    private static final Charset CHARSET = Charsets.UTF_8;

    private static final String EXTRA_SALT = "C,gCW9<e";

    @Value("${application.id}")
    private String applicationName;

    private String password;

    @Override
    public String applicationPassword() {
        return password;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String plain = EXTRA_SALT + applicationName;
        password = new String(Base64.encode(plain.getBytes(CHARSET)), CHARSET);
    }
}
