package com.cumulocity.sdk.client.reliable;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.reliable.notification.NotificationTokenClaimsRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import lombok.RequiredArgsConstructor;
import org.svenson.JSONParser;

import javax.ws.rs.core.Response;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@RequiredArgsConstructor
public class TokenApiImpl implements TokenApi {
    public static final CumulocityMediaType TOKEN_MEDIA_TYPE = new CumulocityMediaType("application", "json");

    public static final String TOKEN_REQUEST_URI = "reliablenotification/token";

    private final PlatformParameters platformParameters;
    private final RestConnector restConnector;

    @Override
    public String create(NotificationTokenClaimsRepresentation tokenClaim) {
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
        return result.getToken();
    }

    @Override
    public Map<String, Object> verify(String token) {
        Response response = restConnector.get(getTokenRequestUri() + "?token=" + token, APPLICATION_JSON_TYPE);
        Map<String, Object> responseJson = null;
        JSONParser parser = new JSONParser();
        responseJson = (Map<String, Object>) parser.parse(response.readEntity(String.class));
        return responseJson;
    }

    private String getTokenRequestUri() {
        return platformParameters.getHost() + TOKEN_REQUEST_URI;
    }
}
