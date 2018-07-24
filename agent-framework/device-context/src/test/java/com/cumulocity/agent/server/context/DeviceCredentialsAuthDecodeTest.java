package com.cumulocity.agent.server.context;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class DeviceCredentialsAuthDecodeTest {

    @Test
    public void shouldDecodeChineseCharFromBase64Format() {

        //given
        String login = "management/你好";
        String password = "chinese@123";
        String authorization = "BASIC bWFuYWdlbWVudC/kvaDlpb06Y2hpbmVzZUAxMjM=";

        String[] loginAndPass = DeviceCredentials.decode(authorization);

        //when
        String loginDecoded    = loginAndPass[0];
        String passwordDecoded  = loginAndPass[1];

        //then
        assertThat(login).isEqualTo(loginDecoded);
        assertThat(password).isEqualTo(passwordDecoded);
    }
}
