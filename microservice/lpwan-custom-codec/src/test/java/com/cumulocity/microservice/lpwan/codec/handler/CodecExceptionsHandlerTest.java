package com.cumulocity.microservice.lpwan.codec.handler;

import com.cumulocity.microservice.customdecoders.api.exception.DecoderServiceException;
import com.cumulocity.microservice.customdecoders.api.exception.InvalidInputDataException;
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.rest.representation.ErrorDetails;
import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.svenson.JSONParser;

import static com.cumulocity.rest.representation.CumulocityMediaType.ERROR_MESSAGE_TYPE;
import static org.junit.jupiter.api.Assertions.*;

class CodecExceptionsHandlerTest {

    @Test
    void doHandleException_InvalidInputDataException() {
        InvalidInputDataException exception = new InvalidInputDataException(new IllegalArgumentException("Missing input parameters."), "Decoder Failed with missing input parameters.", DecoderResult.empty());
        ResponseEntity<DecoderResult> responseEntity = new CodecExceptionsHandler().handleDecoderServiceException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getContentType());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, responseEntity.getHeaders().getContentType().toString());

        assertNotNull(responseEntity.getBody());
        DecoderResult decoderResult = responseEntity.getBody();
        Assert.assertNotNull(decoderResult);
        Assert.assertEquals("Decoder Failed with missing input parameters.", decoderResult.getMessage());
    }

    @Test
    void doHandleException_DecoderServiceException() {
        DecoderServiceException exception = new DecoderServiceException(new Exception("Some internal error."), "Decoder Failed with internal service error.", DecoderResult.empty());
        ResponseEntity<DecoderResult> responseEntity = new CodecExceptionsHandler().handleDecoderServiceException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getContentType());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, responseEntity.getHeaders().getContentType().toString());

        assertNotNull(responseEntity.getBody());
        DecoderResult decoderResult = responseEntity.getBody();
        Assert.assertNotNull(decoderResult);
        Assert.assertEquals("Decoder Failed with internal service error.", decoderResult.getMessage());
    }

    @Test
    void doHandleException_IllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input parameter message");
        ResponseEntity<ErrorMessageRepresentation> responseEntity = new CodecExceptionsHandler().handleExceptionForBadRequest(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_MESSAGE_TYPE, responseEntity.getHeaders().getContentType().toString());

        assertNotNull(responseEntity.getBody());
        ErrorMessageRepresentation errorMessageRepresentation = responseEntity.getBody();
        assertEquals("Codec Microservice Error", errorMessageRepresentation.getError());
        assertEquals(exception.getMessage(), errorMessageRepresentation.getMessage());

        ErrorDetails details = errorMessageRepresentation.getDetails();
        assertNotNull(details);
        assertEquals(IllegalArgumentException.class.getCanonicalName(), details.getExpectionClass());
        assertEquals(exception.getMessage(), details.getExceptionMessage());
    }
}