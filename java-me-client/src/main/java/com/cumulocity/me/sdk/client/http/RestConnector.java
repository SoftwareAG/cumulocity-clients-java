package com.cumulocity.me.sdk.client.http;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public interface RestConnector {

    String X_CUMULOCITY_APPLICATION_KEY = "X-Cumulocity-Application-Key";
    
    String AUTHORIZATION = "Authorization";
    
    CumulocityResourceRepresentation get(String path, CumulocityMediaType mediaType, Class responseType);

    CumulocityResourceRepresentation post(String path, CumulocityMediaType mediaType,
            CumulocityResourceRepresentation representation);

    CumulocityResourceRepresentation put(String path, CumulocityMediaType mediaType,
            CumulocityResourceRepresentation representation);

    void delete(String path);
    
}
