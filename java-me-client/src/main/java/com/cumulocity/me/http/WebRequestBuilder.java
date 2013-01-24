package com.cumulocity.me.http;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public interface WebRequestBuilder {
    
    WebRequestBuilder type(CumulocityMediaType mediaType);
    
    WebRequestBuilder accept(CumulocityMediaType mediaType);
    
    WebRequestBuilder header(String name, Object value);

    CumulocityResourceRepresentation get(int responseStatus, Class responseEntityType);
    
    CumulocityResourceRepresentation post(CumulocityResourceRepresentation requestEntity, int responseStatus, Class responseEntityType);
    
    CumulocityResourceRepresentation put(CumulocityResourceRepresentation requestEntity, int responseStatus, Class responseEntityType);
    
    CumulocityResourceRepresentation delete(int responseStatus, Class responseEntityType);
}