/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.device.codec.exception.handler;

import com.cumulocity.device.codec.exception.DecoderException;
import com.cumulocity.rest.representation.ErrorDetails;
import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.svenson.JSONParser;

import static com.cumulocity.rest.representation.CumulocityMediaType.ERROR_MESSAGE_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CodecExceptionsHandlerTest {

    @Test
    void doHandleException_DecoderException() {
        DecoderException exception = new DecoderException("Test message", new NullPointerException("Null message"));
        ResponseEntity<String> responseEntity = new CodecExceptionsHandler().handleExceptionForInternalServerError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_MESSAGE_TYPE, responseEntity.getHeaders().getContentType().toString());

        assertNotNull(responseEntity.getBody());
        ErrorMessageRepresentation errorMessageRepresentation = JSONParser.defaultJSONParser().parse(ErrorMessageRepresentation.class, responseEntity.getBody());
        assertEquals("Codec Microservice Error", errorMessageRepresentation.getError());
        assertEquals(exception.getMessage(), errorMessageRepresentation.getMessage());

        ErrorDetails details = errorMessageRepresentation.getDetails();
        assertNotNull(details);
        assertEquals(DecoderException.class.getCanonicalName(), details.getExpectionClass());
        assertEquals(exception.getMessage(), details.getExceptionMessage());
    }

    @Test
    void doHandleException_IllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input parameter message");
        ResponseEntity<String> responseEntity = new CodecExceptionsHandler().handleExceptionForBadRequest(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_MESSAGE_TYPE, responseEntity.getHeaders().getContentType().toString());

        assertNotNull(responseEntity.getBody());
        ErrorMessageRepresentation errorMessageRepresentation = JSONParser.defaultJSONParser().parse(ErrorMessageRepresentation.class, responseEntity.getBody());
        assertEquals("Codec Microservice Error", errorMessageRepresentation.getError());
        assertEquals(exception.getMessage(), errorMessageRepresentation.getMessage());

        ErrorDetails details = errorMessageRepresentation.getDetails();
        assertNotNull(details);
        assertEquals(IllegalArgumentException.class.getCanonicalName(), details.getExpectionClass());
        assertEquals(exception.getMessage(), details.getExceptionMessage());
    }
}