package com.cumulocity.me.sdk.client.http;

import javax.microedition.io.HttpConnection;

import com.cumulocity.me.http.WebClient;
import com.cumulocity.me.http.WebRequestBuilder;
import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.sdk.client.PlatformParameters;
import com.cumulocity.me.util.Base64;
import com.cumulocity.me.util.StringUtils;

public class RestConnectorImpl implements RestConnector {

    private final PlatformParameters platformParameters;

    private final WebClient client;

    public RestConnectorImpl(PlatformParameters platformParameters, WebClient client) {
        this.platformParameters = platformParameters;
        this.client = client;
    }

    public CumulocityResourceRepresentation get(String path, CumulocityMediaType mediaType, Class responseEntityType) {
        return authorizedRequest(path).accept(mediaType).get(HttpConnection.HTTP_OK, responseEntityType);
    }

    public CumulocityResourceRepresentation post(String path, CumulocityMediaType mediaType, CumulocityResourceRepresentation representation) {
        WebRequestBuilder builder = authorizedRequest(path).type(mediaType);
        if (platformParameters.isRequireResponseBody()) {
            builder.accept(mediaType);
        }
        return builder.post(representation, HttpConnection.HTTP_CREATED, representation.getClass());
    }

    public CumulocityResourceRepresentation put(String path, CumulocityMediaType mediaType, CumulocityResourceRepresentation representation) {
        WebRequestBuilder builder = authorizedRequest(path).type(mediaType);
        if (platformParameters.isRequireResponseBody()) {
            builder.accept(mediaType);
        }
        return builder.put(representation, HttpConnection.HTTP_OK, representation.getClass());
    }

    public void delete(String path) {
        authorizedRequest(path).delete(HttpConnection.HTTP_NO_CONTENT, null);
    }
    
    private WebRequestBuilder authorizedRequest(String path) {
        return client.request(insertTailIfNeeded(path))
                .header(AUTHORIZATION, getBasicAuthenticationCode(platformParameters))
                .header(X_CUMULOCITY_APPLICATION_KEY, platformParameters.getApplicationKey());
    }
    
    private String getBasicAuthenticationCode(PlatformParameters platformParameters) {
        String username = platformParameters.getTenantId() + "/" + platformParameters.getUser();
        String password = platformParameters.getPassword();
        return "Basic " + new String(Base64.encode(username + ":" + password));
    }
    
    private String insertTailIfNeeded(String path) {
        int indexOf = path.indexOf("?");
        if (indexOf >= 0) {
            return StringUtils.insert(path, indexOf, "/");
        } else {
            return StringUtils.ensureTail(path, "/");
        }
    }
    
}
