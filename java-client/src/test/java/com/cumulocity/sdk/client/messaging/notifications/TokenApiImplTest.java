package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenClaimsRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.cumulocity.sdk.client.messaging.notifications.TokenApiImpl.TOKEN_MEDIA_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenApiImplTest {
    private static final String HOST = "core-0.platform.default.svc.cluster.local/";
    private static final String TOKEN_REQUEST_URI = "reliablenotification/token";
    private static final String JWT_TOKEN = "f4k3_jwt_t0k3n";

    private TokenApi tokenApi;

    @Mock
    private RestConnector restConnector;

    @Mock
    private PlatformParameters platformParameters;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        tokenApi = new TokenApiImpl(platformParameters, restConnector);
        when(platformParameters.getHost()).thenReturn(HOST);
    }

    @Test
    public void shouldCreateToken() {
        //given
        NotificationTokenClaimsRepresentation tokenClaim =
                new NotificationTokenClaimsRepresentation("sub", "sup", 1L);
        Token token = new Token(JWT_TOKEN);
        when(restConnector.post(
                getUri(TOKEN_REQUEST_URI),
                TOKEN_MEDIA_TYPE,
                TOKEN_MEDIA_TYPE,
                tokenClaim,
                Token.class)).thenReturn(token);

        //when
        when(platformParameters.getHost()).thenReturn(HOST);

        //then
        assertThat(tokenApi.create(tokenClaim).getTokenString()).isEqualTo(JWT_TOKEN);
    }

    @Test
    public void shouldBuildCreateTokenUri() {
        //given
        NotificationTokenClaimsRepresentation tokenClaim =
                new NotificationTokenClaimsRepresentation("sub", "sup", 1L);
        final String uri = getUri(TOKEN_REQUEST_URI);
        when(restConnector.post(any(),any(),any(),any(),any())).thenReturn(new Token());

        //when
        tokenApi.create(tokenClaim);

        //then
        verify(restConnector).post(
                eq(uri),
                eq(TOKEN_MEDIA_TYPE),
                eq(TOKEN_MEDIA_TYPE),
                eq(tokenClaim),
                eq(Token.class));
    }

    @Test
    public void shouldVerifyToken() {
        //given
        TokenDetails tokenDetails = new TokenDetails("sub", "topic", "jti", 1L, 1L);
        final String uri = getUri(TOKEN_REQUEST_URI + "?token=" + JWT_TOKEN);
        when(restConnector.get(
                uri,
                TOKEN_MEDIA_TYPE,
                TokenDetails.class)).thenReturn(tokenDetails);

        //when
        TokenDetails verificationResult = tokenApi.verify(JWT_TOKEN);

        //then
        assertThat(verificationResult).isEqualTo(tokenDetails);
    }

    @Test
    public void shouldBuildVerifyUri() {
        //given
        final String uri = getUri(TOKEN_REQUEST_URI + "?token=" + JWT_TOKEN);

        //when
        tokenApi.verify(JWT_TOKEN);

        //then
        verify(restConnector).get(eq(uri), eq(TOKEN_MEDIA_TYPE), eq(TokenDetails.class));
    }

    private String getUri(String endpoint) {
        return HOST + endpoint;
    }
}