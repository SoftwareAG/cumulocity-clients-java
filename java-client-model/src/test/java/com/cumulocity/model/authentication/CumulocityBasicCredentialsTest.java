package com.cumulocity.model.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CumulocityBasicCredentialsTest {

    @Test
    void shouldReadFull() {
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.from("tenant/username:password:pass");
        assertEquals("tenant", credentials.getTenantId());
        assertEquals("username", credentials.getUsername());
        assertEquals("password:pass", credentials.getPassword());
    }

    @Test
    void shouldReadTenantUsername() {
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.from("tenant/username");
        assertEquals("tenant", credentials.getTenantId());
        assertEquals("username", credentials.getUsername());
        assertNull(credentials.getPassword());
    }

    @Test
    void shouldReadUsername() {
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.from("username");
        assertNull(credentials.getTenantId());
        assertEquals("username", credentials.getUsername());
        assertNull(credentials.getPassword());
    }

    @Test
    void shouldReadUsernamePassword() {
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.from("username:password:pass");
        assertNull(credentials.getTenantId());
        assertEquals("username", credentials.getUsername());
        assertEquals("password:pass", credentials.getPassword());
    }

    @Test
    void shouldReturnBasicEncodedAuthenticationString() {
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.from("username:password:pass");
        assertEquals("Basic dXNlcm5hbWU6cGFzc3dvcmQ6cGFzcw==", credentials.getAuthenticationString());
    }

    @Test
    void shouldReturnBasicEncodedAuthenticationStringWithNoAsciiCharacters() {
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.from("management/田中隆太:(Def_Leppard)1");
        assertEquals("Basic bWFuYWdlbWVudC/nlLDkuK3pmoblpKo6KERlZl9MZXBwYXJkKTE=", credentials.getAuthenticationString());
    }

    @Test
    void shouldHidePassword() {
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.builder()
                .username("username")
                .password("password")
                .tenantId("tenId")
                .applicationKey("appKey")
                .requestOrigin("reqOrigin")
                .build();
        assertEquals(
                "CumulocityBasicCredentials{" +
                        "username='username', " +
                        "tenantId='tenId', " +
                        "password='******', " +
                        "applicationKey='appKey', " +
                        "requestOrigin='reqOrigin'}",
                credentials.toString()
        );

    }

    @Test
    void shouldCorrectlyParsePasswordWithSlashesAndColons() {
        String passwordWithSlashesAndColons = "/pa:ss/word/";
        String tenantLoginPassword = "tenantName/userName:" + passwordWithSlashesAndColons;
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.from(tenantLoginPassword);
        assertEquals("tenantName/userName", credentials.getLoginWithTenant());
        assertEquals(credentials.getPassword(), passwordWithSlashesAndColons);
    }

    @Test
    void shouldCorrectlyParsePasswordWithSlashesWhenThereIsNoTenant() {
        String passwordWithSlashesAndColons = "/pa:ss/word/";
        String loginPasswordWithoutTenant = "userName:" + passwordWithSlashesAndColons;
        CumulocityBasicCredentials credentials = CumulocityBasicCredentials.from(loginPasswordWithoutTenant);
        assertEquals("userName", credentials.getLoginWithTenant());
        assertEquals(credentials.getPassword(), passwordWithSlashesAndColons);
    }

}
