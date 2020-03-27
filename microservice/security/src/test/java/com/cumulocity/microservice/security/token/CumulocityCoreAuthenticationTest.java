package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.sdk.client.CumulocityAuthenticationFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandler;
import com.sun.jersey.api.client.ClientRequest;
import java.net.URI;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.jwt.Jwt;

import static com.cumulocity.microservice.security.token.JwtTokenTestsHelper.SAMPLE_XSRF_TOKEN;
import static com.cumulocity.microservice.security.token.JwtTokenTestsHelper.mockedJwtImpl;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CumulocityCoreAuthenticationTest {

    private static final String TENANT_NAME = "test1234";
    private static final String USERNAME = "user1234";

    @Mock
    private JwtCredentials jwtCredentials;

    @Mock
    private ClientRequest clientRequest;

    @Mock
    private MultivaluedMap requestProperties;

    private JwtTokenAuthentication jwtTokenAuthentication;

    @Before
    public void setup() {
        jwtTokenAuthentication = new JwtTokenAuthentication(jwtCredentials);
    }

    @Test
    public void shouldCreateClientWithFilterForCookieAuth() {
        //given
        Jwt accessToken = mockedJwtImpl();
        JwtAndXsrfTokenCredentials jwtAndXsrfTokenCredentials = new JwtAndXsrfTokenCredentials(accessToken, SAMPLE_XSRF_TOKEN);
        JwtTokenAuthentication jwtTokenAuthenticationWithXsrf = new JwtTokenAuthentication(jwtAndXsrfTokenCredentials);
        mockRequest();

        //when
        Client client = CumulocityCoreAuthenticationClient.createClientWithAuthenticationFilter(jwtTokenAuthenticationWithXsrf);
        CumulocityAuthenticationFilter filter = getAuthenticationFilter(client);
        filter.handle(clientRequest);

        //then
        verify(requestProperties).remove(eq("Authorization"));
        verify(requestProperties).putSingle(eq("Cookie"), eq("authorization=" + jwtAndXsrfTokenCredentials.getJwt().getEncoded()));
        verify(requestProperties).putSingle(eq("X-XSRF-TOKEN"), eq(jwtAndXsrfTokenCredentials.getXsrfToken()));
    }

    private void mockRequest() {
        String someMethod = "GET";
        when(clientRequest.getMethod()).thenReturn(someMethod);
        URI someUri = URI.create("https://www.tenant.cumulocity.com");
        when(clientRequest.getURI()).thenReturn(someUri);
        when(clientRequest.getMetadata()).thenReturn(requestProperties);
        when(clientRequest.getHeaders()).thenReturn(requestProperties);
    }

    private CumulocityAuthenticationFilter getAuthenticationFilter(Client client) {
        ClientHandler clientHandler = client.getHeadHandler();
        assertThat(clientHandler).isInstanceOf(CumulocityAuthenticationFilter.class);
        return (CumulocityAuthenticationFilter) clientHandler;
    }

    @Test
    public void shouldCreateClientWithFilterForHeaderAuth() {
        //given
        Jwt accessToken = mockedJwtImpl();
        JwtOnlyCredentials jwtOnlyCredentials = new JwtOnlyCredentials(accessToken);
        JwtTokenAuthentication jwtOnlyTokenAuthentication = new JwtTokenAuthentication(jwtOnlyCredentials);
        mockRequest();

        //when
        Client client = CumulocityCoreAuthenticationClient.createClientWithAuthenticationFilter(jwtOnlyTokenAuthentication);
        CumulocityAuthenticationFilter filter = getAuthenticationFilter(client);
        filter.handle(clientRequest);

        //then
        verify(requestProperties, times(0)).remove(eq("Authorization"));
        verify(requestProperties).add(eq("Authorization"), eq("Bearer " + jwtOnlyCredentials.getJwt().getEncoded()));
        verify(requestProperties, times(0)).putSingle(eq("X-XSRF-TOKEN"), anyString());
    }

    @Test
    public void shouldUpdateAndAddTokenCredentials() {
        //given
        when(jwtCredentials.toUserCredentials(TENANT_NAME, jwtTokenAuthentication)).thenReturn(UserCredentials.builder().tenant(TENANT_NAME).username(USERNAME).build());

        //when
        JwtTokenAuthentication updatedToken = CumulocityCoreAuthenticationClient.updateUserCredentials(TENANT_NAME, jwtTokenAuthentication);

        //then
        assertThat(updatedToken.getUserCredentials().getTenant()).isEqualTo(TENANT_NAME);
        assertThat(updatedToken.getUserCredentials().getUsername()).isEqualTo(USERNAME);
    }
}

