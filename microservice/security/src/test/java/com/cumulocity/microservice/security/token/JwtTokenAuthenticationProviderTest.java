package com.cumulocity.microservice.security.token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.core.Authentication;

import java.util.concurrent.ExecutionException;

import static com.cumulocity.microservice.security.token.JwtTokenTestsHelper.SAMPLE_XSRF_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenAuthenticationProviderTest {

    @Mock
    private StandardEnvironment standardEnvironment;
    @Mock
    private JwtTokenAuthentication updatedJwtTokenAuthenticationFromCache;
    @Mock
    private JwtAuthenticatedTokenCache jwtAuthenticatedTokenCache;

    private JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;
    private JwtAndXsrfTokenCredentials jwtAndXsrfTokenCredentials;

    @Before
    public void setup() {
        jwtTokenAuthenticationProvider = new JwtTokenAuthenticationProvider(standardEnvironment, jwtAuthenticatedTokenCache);
        jwtAndXsrfTokenCredentials = new JwtAndXsrfTokenCredentials(JwtTokenTestsHelper.mockedJwtImpl(), SAMPLE_XSRF_TOKEN);
    }

    @Test
    public void shouldReturnAuthentication() throws ExecutionException {
        //given
        when(jwtAuthenticatedTokenCache.get(any(JwtCredentials.class), any(JwtTokenAuthenticationLoader.class))).thenReturn((updatedJwtTokenAuthenticationFromCache));
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(jwtAndXsrfTokenCredentials);

        //when
        Authentication tokenFromCache = jwtTokenAuthenticationProvider.authenticate(jwtTokenAuthentication);

        //then
        verify(jwtAuthenticatedTokenCache).get(any(JwtCredentials.class), any(JwtTokenAuthenticationLoader.class));
        assertThat(tokenFromCache).isSameAs(updatedJwtTokenAuthenticationFromCache);
    }
}
