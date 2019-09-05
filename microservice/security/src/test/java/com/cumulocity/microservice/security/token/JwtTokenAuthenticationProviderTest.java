package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.core.Authentication;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JwtTokenAuthenticationProvider.class)
public class JwtTokenAuthenticationProviderTest extends JwtTokenTestsHelper {
    @Mock
    private StandardEnvironment standardEnvironment;
    @Mock
    private JwtTokenAuthentication updatedJwtTokenAuthenticationFromCache;
    @Mock
    private JwtAuthenticatedTokenCache jwtAuthenticatedTokenCache;
    @Mock
    CumulocityOAuthUserDetails cumulocityOAuthUserDetailsMock;

    private JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;
    private JwtTokenAuthentication jwtTokenAuthentication;
    private JwtAndXsrfTokenCredentials jwtAndXsrfTokenCredentials;

    @Before
    public void setup() {
        jwtTokenAuthenticationProvider = new JwtTokenAuthenticationProvider(standardEnvironment, jwtAuthenticatedTokenCache);
        jwtAndXsrfTokenCredentials = new JwtAndXsrfTokenCredentials(mockedJwtImpl(), SAMPLE_XSRF_TOKEN);
        jwtTokenAuthentication = new JwtTokenAuthentication(jwtAndXsrfTokenCredentials);
    }

    @Test
    public void shouldReturnAuthenticationWhenTokenInCache() {
        when(jwtAuthenticatedTokenCache.get(jwtAndXsrfTokenCredentials)).thenReturn(Optional.<Authentication>of(updatedJwtTokenAuthenticationFromCache));
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(jwtAndXsrfTokenCredentials);
        Authentication tokenFromCache = jwtTokenAuthenticationProvider.authenticate(jwtTokenAuthentication);

        verify(jwtAuthenticatedTokenCache).get(jwtAndXsrfTokenCredentials);
        assertThat(tokenFromCache).isSameAs(updatedJwtTokenAuthenticationFromCache);
    }

    @Test
    public void shouldUpdateAndAddTokenCredentials() throws Exception {
        CurrentUserRepresentation currUserRepresentation = mockCurrUserRepresentationResponseFromCoreAndAddToCache();
        String tenant = mockResponseFromCoreToGetTenantName(cumulocityOAuthUserDetailsMock);
        PowerMockito.whenNew(CumulocityOAuthUserDetails.class).withAnyArguments().thenReturn(cumulocityOAuthUserDetailsMock);

        JwtTokenAuthentication updatedToken = (JwtTokenAuthentication) jwtTokenAuthenticationProvider.authenticate(jwtTokenAuthentication);

        verify(cumulocityOAuthUserDetailsMock).getCurrentUser();
        verify(jwtAuthenticatedTokenCache).get(jwtAndXsrfTokenCredentials);
        verify(standardEnvironment).getSystemEnvironment();
        verify(cumulocityOAuthUserDetailsMock).getTenantName();
        verify(jwtAuthenticatedTokenCache).put(jwtAndXsrfTokenCredentials, updatedToken);

        tokenShouldBeUpdated(updatedToken, tenant, currUserRepresentation);
    }

    private CurrentUserRepresentation mockCurrUserRepresentationResponseFromCoreAndAddToCache() {
        CurrentUserRepresentation currUserRepresentation = mockCurrentUser();
        when(cumulocityOAuthUserDetailsMock.getCurrentUser()).thenReturn(currUserRepresentation);
        when(jwtAuthenticatedTokenCache.get(jwtAndXsrfTokenCredentials)).thenReturn(Optional.<Authentication>absent());
        return currUserRepresentation;
    }

    private CurrentUserRepresentation mockCurrentUser() {
        CurrentUserRepresentation currUserRepresentation = new CurrentUserRepresentation();
        String username = "User1234";
        currUserRepresentation.setUserName(username);
        return currUserRepresentation;
    }

    private String mockResponseFromCoreToGetTenantName(CumulocityOAuthUserDetails cumulocityOAuthUserDetailsMock) {
        String baseUrl = "sampleUrl.com";
        String key = "C8Y_BASEURL";
        mockStandardEnvironment(key, baseUrl);
        String tenant = "tenant1234";
        when(cumulocityOAuthUserDetailsMock.getTenantName()).thenReturn(tenant);
        return tenant;
    }

    private void mockStandardEnvironment(String key, String value) {
        Map<String, Object> sysEnv = new HashMap<String, Object>();
        sysEnv.put(key, value);
        when(standardEnvironment.getSystemEnvironment()).thenReturn(sysEnv);
    }

    private void tokenShouldBeUpdated(JwtTokenAuthentication updatedToken, String tenant, CurrentUserRepresentation currUserRepresentation) {
        UserCredentials updatedUserCredentials = updatedToken.getUserCredentials();
        assertThat(updatedUserCredentials.getTenant()).isEqualTo(tenant);
        String username = currUserRepresentation.getUserName();
        assertThat(updatedUserCredentials.getUsername()).isEqualTo(username);
        assertThat(updatedUserCredentials.getOAuthAccessToken()).isEqualTo(SAMPLE_ENCODED_TOKEN);
        assertThat(updatedUserCredentials.getXsrfToken()).isEqualTo(SAMPLE_XSRF_TOKEN);
    }

}
