/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.handler;

import com.cumulocity.lpwan.exception.InputDataValidationException;
import com.cumulocity.lpwan.exception.LpwanServiceException;
import com.cumulocity.rest.representation.ErrorDetails;
import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.cumulocity.rest.representation.CumulocityMediaType.ERROR_MESSAGE_TYPE;

/**
 * The <b>LpwanExceptionsHandler</b> is a custom exception handler.
 *
 */

@ControllerAdvice
@Slf4j
public class LpwanExceptionsHandler {

    /**
     * This method handles the custom <b>{@link com.cumulocity.lpwan.exception.LpwanServiceException}</b>.
     *
     * @param exception       represents the exception
     * @return ResponseEntity <code>HttpStatus.INTERNAL_SERVER_ERROR</code> or <code>HttpStatus.BAD_REQUEST</code>
     * @see com.cumulocity.lpwan.exception.LpwanServiceException {@link com.cumulocity.lpwan.exception.LpwanServiceException}
     */
    @ExceptionHandler(value = LpwanServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessageRepresentation> handleLpwanServiceException(LpwanServiceException exception) {
        log.error(exception.getMessage(), exception);

        if (exception instanceof InputDataValidationException) {
            return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
        }
        else {
            return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method handles the <b>UnsupportedOperationException</b>.
     *
     * @param exception       represents the exception
     * @return ResponseEntity <code>HttpStatus.INTERNAL_SERVER_ERROR</code>
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/UnsupportedOperationException.html">UnsupportedOperationException</a>
     */
    @ExceptionHandler(value = UnsupportedOperationException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessageRepresentation> handleExceptionForInternalServerError(Throwable exception) {
        log.error(exception.getMessage(), exception);
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method handles the <b>IllegalArgumentException</b>.
     *
     * @param exception       represents the exception
     * @return ResponseEntity <code>HttpStatus.BAD_REQUEST</code>
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">IllegalArgumentException</a>
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessageRepresentation> handleExceptionForBadRequest(Throwable exception) {
        log.error(exception.getMessage(), exception);
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ErrorMessageRepresentation> buildErrorResponse(Throwable exception, HttpStatus status) {
        ErrorMessageRepresentation representation = buildErrorMessageRepresentation(exception);
        return ResponseEntity.status(status).contentType(MediaType.parseMediaType(ERROR_MESSAGE_TYPE)).body(representation);
    }

    private ErrorMessageRepresentation buildErrorMessageRepresentation(Throwable exception) {
        ErrorMessageRepresentation representation = new ErrorMessageRepresentation();
        representation.setError("Lpwan Backend Error");
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
