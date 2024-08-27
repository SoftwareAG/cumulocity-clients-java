package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
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

    // Fix of https://cumulocity.atlassian.net/browse/MTM-60160
    @Test
    public void shouldReturnNameToIncludeTenantAndUserNameToMatch() {
        // given
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(credentials);
        jwtTokenAuthentication.setUserCredentials(UserCredentials.builder().tenant(TENANT_NAME).build());
        CurrentUserRepresentation currentUser = new CurrentUserRepresentation();
        currentUser.setUserName("user");
        jwtTokenAuthentication.setCurrentUserRepresentation(currentUser);
        //when
        String tenantName = jwtTokenAuthentication.getName();

        //then
        assertThat(tenantName).isEqualTo(TENANT_NAME + "/" + currentUser.getUserName());
    }


}
