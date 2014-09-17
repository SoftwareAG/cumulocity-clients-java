/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cumulocity.sdk.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.ResourceRepresentation;
import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import com.sun.jersey.api.client.ClientResponse;

public class ResponseParser {
    
    public static final String NO_ERROR_REPRESENTATION = "Something went wrong. Failed to parse error message.";
    private static final Logger LOG = LoggerFactory.getLogger(ResponseParser.class);

    public <T extends ResourceRepresentation> T parse(ClientResponse response, int expectedStatusCode,
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
            ErrorMessageRepresentation errorRepresentation = getErrorRepresentation(response);
            if (errorRepresentation == null) {
                return NO_ERROR_REPRESENTATION;
            }
            errorMessage += "\n" + errorRepresentation;
        }
        return errorMessage;
    }

    private ErrorMessageRepresentation getErrorRepresentation(ClientResponse response) {
        try {
            return response.getEntity(ErrorMessageRepresentation.class);
        } catch (Exception e) {
            LOG.error("Failed to parse error message: " + getErrorString(response), e);
            return null;
        }
    }

    private String getErrorString(ClientResponse response) {
        try {
            return response.getEntity(String.class);
        } catch (Exception e) {
            LOG.error("Couldn't parse error message to string.");
            return "";
        }
    }

    public GId parseIdFromLocation(ClientResponse response) {
        String path;
        path = response.getLocation().getPath();
        String[] pathParts = path.split("\\/");
        String gid = pathParts[pathParts.length - 1];
        return new GId(gid);
    }
}
