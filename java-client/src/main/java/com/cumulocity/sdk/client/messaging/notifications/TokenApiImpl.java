package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.reliable.notification.NotificationTokenClaimsRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TokenApiImpl implements TokenApi {
    public static final CumulocityMediaType TOKEN_MEDIA_TYPE = new CumulocityMediaType("application", "json");

    public static final String TOKEN_REQUEST_URI = "reliablenotification/token";

    private final PlatformParameters platformParameters;
    private final RestConnector restConnector;

    @Override
    public Token create(NotificationTokenClaimsRepresentation tokenClaim) throws IllegalArgumentException, SDKException  {
        if (tokenClaim == null) {
            throw new IllegalArgumentException("Token claim is null");
        }

        Token result = restConnector.post(
                getTokenRequestUri(),
                TOKEN_MEDIA_TYPE,
                TOKEN_MEDIA_TYPE,
                tokenClaim,
                Token.class
        );
        return result;
    }

    @Override
    public TokenDetails verify(String token) throws SDKException {
        TokenDetails tokenDetails = restConnector
                .get(getTokenRequestUri() + "?token=" + token,
                        TOKEN_MEDIA_TYPE,
                        TokenDetails.class);
        return tokenDetails;
    }

    private String getTokenRequestUri() {
        return platformParameters.getHost() + TOKEN_REQUEST_URI;
    }
}
