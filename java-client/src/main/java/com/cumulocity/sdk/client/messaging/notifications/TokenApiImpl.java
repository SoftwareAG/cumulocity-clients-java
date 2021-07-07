package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.reliable.notification.NotificationTokenRequestRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.svenson.JSONParser;

import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class TokenApiImpl implements TokenApi {
    public static final CumulocityMediaType TOKEN_MEDIA_TYPE = new CumulocityMediaType("application", "json");

    public static final String TOKEN_REQUEST_URI = "reliablenotification/token";
    private static final String JWT_TOKEN_SPLIT = "\\.";
    private static final String TOPIC_SPLIT = "/";

    private final PlatformParameters platformParameters;
    private final RestConnector restConnector;

    @Override
    public Token create(NotificationTokenRequestRepresentation tokenClaim) throws IllegalArgumentException, SDKException {
        if (tokenClaim == null) {
            throw new IllegalArgumentException("Token claim is null");
        }

        return restConnector.post(
                getTokenRequestUri(),
                TOKEN_MEDIA_TYPE,
                TOKEN_MEDIA_TYPE,
                tokenClaim,
                Token.class
        );
    }

    @Override
    public TokenClaims verify(Token token) throws SDKException {
        return restConnector
                .get(getTokenRequestUri() + "?token=" + token.getTokenString(),
                        TOKEN_MEDIA_TYPE,
                        TokenClaims.class);
    }

    @Override
    public Token refresh(Token expiredToken) throws IllegalArgumentException, SDKException {
        if (expiredToken == null) {
            throw new IllegalArgumentException("Expired token is null");
        }
        String[] tokenParts = expiredToken.getTokenString().split(JWT_TOKEN_SPLIT);
        String claimsString = new String(Base64.getDecoder().decode(tokenParts[1]));

        TokenClaims parsedToken = JSONParser.defaultJSONParser().parse(TokenClaims.class, claimsString);

        long expiry = parsedToken.getExp() - parsedToken.getIat();
        long validityPeriodMinutes = expiry / 60;

        // TODO follow up with Kacper
        return create(new NotificationTokenRequestRepresentation(
                parsedToken.getSubscription(),
                parsedToken.getTopic().split(TOPIC_SPLIT)[2],
                validityPeriodMinutes, false));
    }


    private String getTokenRequestUri() {
        return platformParameters.getHost() + TOKEN_REQUEST_URI;
    }
}
