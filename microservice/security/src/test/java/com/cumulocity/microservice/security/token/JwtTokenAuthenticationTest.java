package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenAuthenticationTest {

    private static final String TENANT_NAME = "super-tenant-name";
    private JwtCredentials credentials;

    @Test
    public void shouldGetTenantNameWhenIsPresentInUserCredentials() {
        // given
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(credentials);
        jwtTokenAuthentication.setUserCredentials(UserCredentials.builder().tenant(TENANT_NAME).build());

        //when
        String tenantName = jwtTokenAuthentication.getTenantName();

        //then
        assertThat(tenantName).isEqualTo(TENANT_NAME);
    }

    @Test
    public void shouldGetNullTenantNameWhenIsNotPresentInUserCredentials() {
        // given
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(credentials);
        jwtTokenAuthentication.setUserCredentials(UserCredentials.builder().build());

        //when
        String tenantName = jwtTokenAuthentication.getTenantName();

        //then
        assertThat(tenantName).isNull();
    }

    @Test
    public void shouldGetNullTenantNameWhenUserCredentialsAreMissing() {
        // given
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(credentials);

        //when
        String tenantName = jwtTokenAuthentication.getTenantName();

        //then
        assertThat(tenantName).isNull();
    }

}