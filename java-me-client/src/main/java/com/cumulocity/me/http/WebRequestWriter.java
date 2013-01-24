package com.cumulocity.me.http;

import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public interface WebRequestWriter {

    byte[] write(WebMethod method, CumulocityResourceRepresentation entity);
}
