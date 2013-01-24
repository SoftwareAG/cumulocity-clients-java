/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.BaseCumulocityResourceRepresentationIf;
import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import com.sun.jersey.api.client.ClientResponse;

public class ResponseParser {

    public <T extends BaseCumulocityResourceRepresentationIf> T parse(ClientResponse response, int expectedStatusCode,
            Class<T> type) throws SDKException {

        checkStatus(response, expectedStatusCode);
        return response.getEntity(type);
    }

    public void checkStatus(ClientResponse response, int expectedStatusCode) throws SDKException {
        int status = response.getStatus();
        if (status != expectedStatusCode) {
            throw new SDKException(status, getErrorMessage(response, status));
        }
    }

    private String getErrorMessage(ClientResponse response, int status) {
        String errorMessage = "Http status code: " + status;

        if (response.hasEntity()) {
            errorMessage += "\n" + response.getEntity(ErrorMessageRepresentation.class);
        }
        return errorMessage;
    }

    public GId parseIdFromLocation(ClientResponse response) {
        String path;
        path = response.getLocation().getPath();
        String[] pathParts = path.split("\\/");
        String gid = pathParts[pathParts.length - 1];
        return new GId(gid);
    }
}
