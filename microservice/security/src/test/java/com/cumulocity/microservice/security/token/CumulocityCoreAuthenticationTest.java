package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import static com.cumulocity.microservice.security.token.JwtTokenTestsHelper.*;
import com.cumulocity.model.authentication.AuthenticationMethod;
import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import com.cumulocity.sdk.client.CumulocityAuthenticationFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandler;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.security.jwt.Jwt;

@RunWith(MockitoJUnitRunner.class)
public class CumulocityCoreAuthenticationTest {

    private static final String BASE_URL = "someUrl";
    private static final String TENANT_NAME = "test1234";
    private static final String USERNAME = "user1234";

    @Mock
    private JwtCredentials jwtCredentials;
    private JwtTokenAuthentication jwtTokenAuthentication;

    @Before
    public void setup() {
        jwtTokenAuthentication = new JwtTokenAuthentication(jwtCredentials);
        when(jwtCredentials.getJwt()).thenReturn(mockedJwtImpl());
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
        return Whitebox.getInternalState(filterForXsrf, "credentials");
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
