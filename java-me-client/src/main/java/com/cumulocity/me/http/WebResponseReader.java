package com.cumulocity.me.http;

import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public interface WebResponseReader {

    CumulocityResourceRepresentation read(WebResponse response, int expectedStatus, Class expectedEntityType);
}
