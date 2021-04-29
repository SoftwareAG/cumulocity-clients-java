package com.cumulocity.sdk.client.reliable;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenClaimsRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.cumulocity.sdk.client.reliable.TokenApiImpl.TOKEN_MEDIA_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class TokenApiImplTest {
    private static final String HOST = "core-0.platform.default.svc.cluster.local:8181";

    private static final String TOKEN_REQUEST_URI = "reliablenotification/token";

    private static final String JWT_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzYWIiLCJ0b3BpYyI6Im1hbmFnZW1lbnQvcmVsbm90aWYvc3ViIiwianRpIjoiZTNkMzE1Y2MtYTE0NC00ZWNhLTk2OGItNmIzNTJjNWYwOWYyIiwiaWF0IjoxNjE5Njk0Mjc3LCJleHAiOjE2MTk3ODA2Nzd9.tjX1WxjFdoissHvQc0Y88Ase6muJFi9xIWC4WmRRHtsc_IkxcPCPyfDzOVW30SqMDqc3PzxEkN9l21D1LfVg06xhFc7o-Ita6a3C3BbuuP0kM5KQCQBXHHcaZsphmZgWXvV-q9SLrQ_3ir3I7OLdipkrJ4QJV9MTWfM-pIAoyTSOr4Eik5osnkPsEJ8P4ZFjCgvB5k1DrwfcOOz19q__dKhftIkhOT7YxxXm20brdUrlb8ZEdwu_PDk5AfoOYYp97pjMO0bTRSgQVf7qFdyMEcU-BuedY45j58qV6-YDWJ6Ep_feVquUUAvVmYH-4JDdYndokb3vk3uLRwHuQwg6Uw";

    TokenApi tokenApi;

    @Mock
    private RestConnector restConnector;

    @Mock
    private PlatformParameters platformParameters;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        tokenApi = new TokenApiImpl(platformParameters, restConnector);
    }

    @Test
    public void shouldCreateToken() {
        NotificationTokenClaimsRepresentation tokenClaim =
                new NotificationTokenClaimsRepresentation("sub", "sup", 1L);
        Token token = new Token(JWT_TOKEN);

        when(restConnector.post(
                getUri(TOKEN_REQUEST_URI),
                TOKEN_MEDIA_TYPE,
                TOKEN_MEDIA_TYPE,
                tokenClaim,
                Token.class))
                .thenReturn(token);
        when(platformParameters.getHost()).thenReturn(HOST);

        assertThat(tokenApi.create(tokenClaim)).isEqualTo(JWT_TOKEN);
    }

    private String getUri(String endpoint) {
        return HOST + endpoint;
    }
}