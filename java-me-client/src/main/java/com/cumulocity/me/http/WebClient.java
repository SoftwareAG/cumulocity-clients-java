package com.cumulocity.me.http;

import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public interface WebClient {

    WebRequestBuilder request(String path);

    WebResponse handle(WebRequest request);

    CumulocityResourceRepresentation handle(WebRequest request, int expectedStatus, Class responseEntityType);
}
