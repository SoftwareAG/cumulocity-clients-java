package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenRequestRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static com.cumulocity.sdk.client.messaging.notifications.TokenApiImpl.TOKEN_MEDIA_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenApiImplTest {
    private static final String HOST = "core-0.platform.default.svc.cluster.local/";
    private static final String JWT_TOKEN = "f4k3_jwt_t0k3n";

    private TokenApi tokenApi;

    @Mock
    private RestConnector restConnector;

    @Mock
    private PlatformParameters platformParameters;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        tokenApi = Mockito.spy(new TokenApiImpl(platformParameters, restConnector));
        when(platformParameters.getHost()).thenReturn(HOST);
    }

    @Test
    public void shouldCreateToken() {
        //given
        NotificationTokenRequestRepresentation tokenRequest =
                new NotificationTokenRequestRepresentation("sub", "sup", 1L, false);
        Token token = new Token(JWT_TOKEN);

        //when
        when(restConnector.post(
                getUri(TokenApiImpl.TOKEN_REQUEST_URI),
                TOKEN_MEDIA_TYPE,
                TOKEN_MEDIA_TYPE,
                tokenRequest,
                Token.class)).thenReturn(token);

        //then
        assertThat(tokenApi.create(tokenRequest).getTokenString()).isEqualTo(JWT_TOKEN);
    }

    @Test
    public void shouldBuildCreateTokenUri() {
        //given
        NotificationTokenRequestRepresentation tokenRequest =
                new NotificationTokenRequestRepresentation("sub", "sup", 1L, false);
        final String uri = getUri(TokenApiImpl.TOKEN_REQUEST_URI);
        when(restConnector.post(any(), any(), any(), any(), any())).thenReturn(new Token());

        //when
        tokenApi.create(tokenRequest);

        //then
        verify(restConnector).post(
                eq(uri),
                eq(TOKEN_MEDIA_TYPE),
                eq(TOKEN_MEDIA_TYPE),
                eq(tokenRequest),
                eq(Token.class));
    }

    @Test
    public void shouldVerifyToken() {
        //given
        TokenClaims tokenRequest = new TokenClaims("sub", "topic", "jti", 1L, 1L);
        final String uri = getUri(TokenApiImpl.TOKEN_REQUEST_URI + "?token=" + JWT_TOKEN);
        Token tokenToVerify = new Token(JWT_TOKEN);

        when(restConnector.get(
                uri,
                TOKEN_MEDIA_TYPE,
                TokenClaims.class)).thenReturn(tokenRequest);

        //when
        TokenClaims verificationResult = tokenApi.verify(tokenToVerify);

        //then
        assertThat(verificationResult).isEqualTo(tokenRequest);
    }

    @Test
    public void shouldBuildVerifyUri() {
        //given
        final String uri = getUri(TokenApiImpl.TOKEN_REQUEST_URI + "?token=" + JWT_TOKEN);
        Token tokenToVerify = new Token(JWT_TOKEN);

        //when
        tokenApi.verify(tokenToVerify);

        //then
        verify(restConnector).get(eq(uri), eq(TOKEN_MEDIA_TYPE), eq(TokenClaims.class));
    }

    @Test
    public void shouldRefreshToken() {
        //given
        String expiredJwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0c3Vic2NyaWJlciIsInRvcGljIjoibWFuYWdlbWVudC9yZW" +
                "xub3RpZi90ZXN0c3Vic2NyaXB0aW9uIiwianRpIjoiMjYwNjY1ZmQtNDI1ZC00NjVlLWJlZTYtZTgzYzI1ZmMxMzYxIiwiaWF0Ij" +
                "oxNjI1NzY5NzUyLCJleHAiOjE2MjU3Njk4MTJ9.KeFUl0b3EMxnlDsin3i8Y_dxidQJmLsbzNSK2JissnYMBSG9EA-YTDNVRwGqW" +
                "LjR8OMEoSiYLPgMPBvWTKKYJliIyStdQ8XhaINHZiwV4Jd-_Y7ITHuc5-XRPN8p2ik1omFmpAS5FwxNsVMj-Rx_dMUK4gp5sKbYr" +
                "R14R1hzFestBZdMnWIT-T5ORywZHd7MtOE7nsSrCHwp6MKmcGvIM7Bhz2e1QC0DU60prpnt_DUoL6M8dpNBPtl40XssGnCIGNruk" +
                "ukm7QMwhgL8U82AQQ_qefpXFJOLMzyDCYD59fMHTQ8Bdi9svH8f6rswu8yQ326QH0sf_Mrhr5dwCI1EnA";
        ArgumentCaptor<NotificationTokenRequestRepresentation> argumentCaptor = ArgumentCaptor.forClass(NotificationTokenRequestRepresentation.class);

        //when
        tokenApi.refresh(new Token(expiredJwtToken));

        verify(tokenApi).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getSubscriber()).isEqualTo("testsubscriber");
        assertThat(argumentCaptor.getValue().getSubscription()).isEqualTo("testsubscription");
        assertThat(argumentCaptor.getValue().getExpiresInMinutes()).isEqualTo(1L);
    }

    private String getUri(String endpoint) {
        return HOST + endpoint;
    }
}