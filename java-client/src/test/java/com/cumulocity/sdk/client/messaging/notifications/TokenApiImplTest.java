package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenClaimsRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

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
        tokenApi = Mockito.spy(new TokenApiImpl(platformParameters, restConnector));
        when(platformParameters.getHost()).thenReturn(HOST);
    }

    @Test
    public void shouldCreateToken() {
        //given
        NotificationTokenClaimsRepresentation tokenClaim =
                new NotificationTokenClaimsRepresentation("sub", "sup", 1L);
        Token token = new Token(JWT_TOKEN);

        //when
        when(restConnector.post(
                getUri(TOKEN_REQUEST_URI),
                TOKEN_MEDIA_TYPE,
                TOKEN_MEDIA_TYPE,
                tokenClaim,
                Token.class)).thenReturn(token);

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
        TokenClaims TokenClaims = new TokenClaims("sub", "topic", "jti", 1L, 1L);
        final String uri = getUri(TOKEN_REQUEST_URI + "?token=" + JWT_TOKEN);
        Token tokenToVerify = new Token(JWT_TOKEN);

        when(restConnector.get(
                uri,
                TOKEN_MEDIA_TYPE,
                TokenClaims.class)).thenReturn(TokenClaims);

        //when
        TokenClaims verificationResult = tokenApi.verify(tokenToVerify);

        //then
        assertThat(verificationResult).isEqualTo(TokenClaims);
    }

    @Test
    public void shouldBuildVerifyUri() {
        //given
        final String uri = getUri(TOKEN_REQUEST_URI + "?token=" + JWT_TOKEN);
        Token tokenToVerify = new Token(JWT_TOKEN);

        //when
        tokenApi.verify(tokenToVerify);

        //then
        verify(restConnector).get(eq(uri), eq(TOKEN_MEDIA_TYPE), eq(TokenClaims.class));
    }

    @Test
    public void shouldRefreshToken() {
        //given
        String expiredJwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzYWIiLCJ0b3BpYyI6Im1hbmFnZW1lbnQvcmV" +
                "sbm90aWYvc3ViIiwianRpIjoiZTNkMzE1Y2MtYTE0NC00ZWNhLTk2OGItNmIzNTJjNWYwOWYyIiwiaWF0IjoxNjE5Njk0Mjc3" +
                "LCJleHAiOjE2MTk3ODA2Nzd9.tjX1WxjFdoissHvQc0Y88Ase6muJFi9xIWC4WmRRHtsc_IkxcPCPyfDzOVW30SqMDqc3PzxE" +
                "kN9l21D1LfVg06xhFc7o-Ita6a3C3BbuuP0kM5KQCQBXHHcaZsphmZgWXvV-q9SLrQ_3ir3I7OLdipkrJ4QJV9MTWfM-pIAoy" +
                "TSOr4Eik5osnkPsEJ8P4ZFjCgvB5k1DrwfcOOz19q__dKhftIkhOT7YxxXm20brdUrlb8ZEdwu_PDk5AfoOYYp97pjMO0bTRS" +
                "gQVf7qFdyMEcU-BuedY45j58qV6-YDWJ6Ep_feVquUUAvVmYH-4JDdYndokb3vk3uLRwHuQwg6Uw";
        ArgumentCaptor<NotificationTokenClaimsRepresentation> argumentCaptor = ArgumentCaptor.forClass(NotificationTokenClaimsRepresentation.class);

        //when
        tokenApi.refresh(new Token(expiredJwtToken));

        verify(tokenApi).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getSubscription()).isEqualTo("sub");
        assertThat(argumentCaptor.getValue().getSubscriber()).isEqualTo("sab");
        assertThat(argumentCaptor.getValue().getExpiresInMinutes()).isEqualTo(1440);
    }

    private String getUri(String endpoint) {
        return HOST + endpoint;
    }
}