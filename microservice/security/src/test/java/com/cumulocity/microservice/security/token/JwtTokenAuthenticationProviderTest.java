package com.cumulocity.microservice.security.token;

import static com.cumulocity.microservice.security.token.JwtTokenTestsHelper.SAMPLE_XSRF_TOKEN;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.core.Authentication;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JwtTokenAuthenticationProvider.class)
public class JwtTokenAuthenticationProviderTest {
    @Mock
    private StandardEnvironment standardEnvironment;
    @Mock
    private JwtTokenAuthentication updatedJwtTokenAuthenticationFromCache;
    @Mock
    private JwtAuthenticatedTokenCache jwtAuthenticatedTokenCache;


    private JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;
    private JwtTokenAuthentication jwtTokenAuthentication;
    private JwtAndXsrfTokenCredentials jwtAndXsrfTokenCredentials;

    @Before
    public void setup() {
        jwtTokenAuthenticationProvider = new JwtTokenAuthenticationProvider(standardEnvironment, jwtAuthenticatedTokenCache);
        jwtAndXsrfTokenCredentials = new JwtAndXsrfTokenCredentials(JwtTokenTestsHelper.mockedJwtImpl(), SAMPLE_XSRF_TOKEN);
        jwtTokenAuthentication = new JwtTokenAuthentication(jwtAndXsrfTokenCredentials);
    }

    @Test
    public void shouldReturnAuthentication() throws ExecutionException {
        when(jwtAuthenticatedTokenCache.get(any(JwtCredentials.class), any(JwtTokenAuthenticationLoader.class))).thenReturn((updatedJwtTokenAuthenticationFromCache));
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(jwtAndXsrfTokenCredentials);

        Authentication tokenFromCache = jwtTokenAuthenticationProvider.authenticate(jwtTokenAuthentication);

        verify(jwtAuthenticatedTokenCache).get(any(JwtCredentials.class), any(JwtTokenAuthenticationLoader.class));
        assertThat(tokenFromCache).isSameAs(updatedJwtTokenAuthenticationFromCache);
    }
}
