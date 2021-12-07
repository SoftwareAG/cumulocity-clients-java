/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.lpwan.codec.handler;

import com.cumulocity.microservice.customdecoders.api.exception.DecoderServiceException;
import com.cumulocity.microservice.customdecoders.api.exception.InvalidInputDataException;
import com.cumulocity.rest.representation.ErrorDetails;
import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.svenson.JSON;

import static com.cumulocity.rest.representation.CumulocityMediaType.ERROR_MESSAGE_TYPE;

/**
 * The <b>CodecExceptionsHandler</b> is a custom exception handler.
 *
 */

@ControllerAdvice
@Slf4j
public class CodecExceptionsHandler {

    /**
     * This method handles the custom <b>DecoderServiceException</b>.
     *
     * @param exception the exception
     * @return ResponseEntity<String> <code>HttpStatus.INTERNAL_SERVER_ERROR</code> or <code>HttpStatus.BAD_REQUEST</code>
     * @see DecoderServiceException
     */
    @ExceptionHandler(value = DecoderServiceException.class)
    @ResponseBody
    public ResponseEntity<Object> handleDecoderServiceException(DecoderServiceException exception) {
        log.error(exception.getMessage(), exception);

        String errorJson = JSON.defaultJSON().forValue(exception.getResult());
        if (exception instanceof InvalidInputDataException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorJson);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorJson);
        }
    }

    /**
     * This method handles the custom <b>DecoderException</b> and <b>UnsupportedOperationException</b>.
     *
     * @param exception the exception
     * @return ResponseEntity<String> <code>HttpStatus.INTERNAL_SERVER_ERROR</code>
     * @see UnsupportedOperationException
     */
    @ExceptionHandler(value = UnsupportedOperationException.class)
    @ResponseBody
    public ResponseEntity<String> handleExceptionForInternalServerError(Throwable exception) {
        log.error(exception.getMessage(), exception);
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method handles the <b>IllegalArgumentException</b>.
     *
     * @param exception the exception
     * @return ResponseEntity<String> <code>HttpStatus.BAD_REQUEST</code>
     * @see IllegalArgumentException
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<String> handleExceptionForBadRequest(Throwable exception) {
        log.error(exception.getMessage(), exception);
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> buildErrorResponse(Throwable exception, HttpStatus status) {
        ErrorMessageRepresentation representation = buildErrorMessageRepresentation(exception);
        String errorJson = JSON.defaultJSON().forValue(representation);
        return ResponseEntity.status(status).contentType(MediaType.parseMediaType(ERROR_MESSAGE_TYPE)).body(errorJson);
    }

    private ErrorMessageRepresentation buildErrorMessageRepresentation(Throwable exception) {
        ErrorMessageRepresentation representation = new ErrorMessageRepresentation();
        representation.setError("Codec Microservice Error");
        representation.setMessage(exception.getMessage());
        representation.setDetails(buildErrorDetails(exception));
        return representation;
    }

    private ErrorDetails buildErrorDetails(Throwable exception) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setExpectionClass(exception.getClass().getCanonicalName());
        errorDetails.setExceptionMessage(exception.getMessage());
        return errorDetails;
    }
}
