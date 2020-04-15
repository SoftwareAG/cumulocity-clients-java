package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.model.authentication.AuthenticationMethod;
import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import com.cumulocity.sdk.client.CumulocityAuthenticationFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;

import static com.cumulocity.microservice.security.token.JwtTokenTestsHelper.SAMPLE_XSRF_TOKEN;
import static com.cumulocity.microservice.security.token.JwtTokenTestsHelper.mockedJwtImpl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CumulocityCoreAuthenticationTest {

    private static final String TENANT_NAME = "test1234";
    private static final String USERNAME = "user1234";

    @Mock
    private JwtCredentials jwtCredentials;
    private JwtTokenAuthentication jwtTokenAuthentication;

    @BeforeEach
    public void setup() {
        jwtTokenAuthentication = new JwtTokenAuthentication(jwtCredentials);
    }

    @Test
    public void shouldCreateClientWithFilterForCookieAuth() {
        Jwt accessToken = mockedJwtImpl();
        JwtAndXsrfTokenCredentials jwtAndXsrfTokenCredentials = new JwtAndXsrfTokenCredentials(accessToken, SAMPLE_XSRF_TOKEN);
        JwtTokenAuthentication jwtTokenAuthenticationWithXsrf = new JwtTokenAuthentication(jwtAndXsrfTokenCredentials);

        Client client = CumulocityCoreAuthenticationClient.createClientWithAuthenticationFilter(jwtTokenAuthenticationWithXsrf);

        CumulocityOAuthCredentials credentials = retrieveOAuthCredentialsFromFilter(client);
        assertThat(credentials.getAuthenticationMethod()).isEqualTo(AuthenticationMethod.COOKIE);
        assertThat(credentials.getAuthenticationString()).isEqualTo(accessToken.getEncoded());
        assertThat(credentials.getXsrfToken()).isEqualTo(SAMPLE_XSRF_TOKEN);
    }

    private CumulocityOAuthCredentials retrieveOAuthCredentialsFromFilter(Client client) {
        ClientHandler clientHandler = client.getHeadHandler();
        assertThat(clientHandler).isInstanceOf(CumulocityAuthenticationFilter.class);
        CumulocityAuthenticationFilter filterForXsrf = (CumulocityAuthenticationFilter) clientHandler;
        return (CumulocityOAuthCredentials )ReflectionTestUtils.getField(filterForXsrf, CumulocityOAuthCredentials.class, "credentials");
    }

    @Test
    public void shouldCreateClientWithFilterForHeaderAuth() {
        Jwt accessToken = mockedJwtImpl();
        JwtOnlyCredentials jwtOnlyCredentials = new JwtOnlyCredentials(accessToken);
        JwtTokenAuthentication jwtOnlyTokenAuthentication = new JwtTokenAuthentication(jwtOnlyCredentials);

        Client client = CumulocityCoreAuthenticationClient.createClientWithAuthenticationFilter(jwtOnlyTokenAuthentication);

        CumulocityOAuthCredentials credentials = retrieveOAuthCredentialsFromFilter(client);
        assertThat(credentials.getAuthenticationMethod()).isEqualTo(AuthenticationMethod.HEADER);
        assertThat(credentials.getAuthenticationString()).isEqualTo("Bearer " + accessToken.getEncoded());
        assertThat(credentials.getXsrfToken()).isEqualTo(null);
    }

    @Test
    public void shouldUpdateAndAddTokenCredentials() {
        when(jwtCredentials.toUserCredentials(TENANT_NAME, jwtTokenAuthentication)).thenReturn(UserCredentials.builder().tenant(TENANT_NAME).username(USERNAME).build());

        JwtTokenAuthentication updatedToken = CumulocityCoreAuthenticationClient.updateUserCredentials(TENANT_NAME, jwtTokenAuthentication);

        assertThat(updatedToken.getUserCredentials().getTenant()).isEqualTo(TENANT_NAME);
        assertThat(updatedToken.getUserCredentials().getUsername()).isEqualTo(USERNAME);
    }
}
