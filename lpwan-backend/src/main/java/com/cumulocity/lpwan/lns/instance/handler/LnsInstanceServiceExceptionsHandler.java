/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.handler;

import com.cumulocity.lpwan.handler.LpwanExceptionsHandler;
import com.cumulocity.lpwan.lns.instance.exception.InvalidInputDataException;
import com.cumulocity.lpwan.lns.instance.exception.LnsInstanceServiceException;
import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The <b>LpwanExceptionsHandler</b> is a custom exception handler.
 *
 */

@ControllerAdvice
@Slf4j
public class LnsInstanceServiceExceptionsHandler extends LpwanExceptionsHandler {

    /**
     * This method handles the custom <b>{@link LnsInstanceServiceException}</b>.
     *
     * @param exception       represents the exception
     * @return ResponseEntity <code>HttpStatus.INTERNAL_SERVER_ERROR</code> or <code>HttpStatus.BAD_REQUEST</code>
     * @see LnsInstanceServiceException {@link LnsInstanceServiceException}
     */
    @ExceptionHandler(value = LnsInstanceServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessageRepresentation> handleLnsInstanceServiceException(LnsInstanceServiceException exception) {
        log.error(exception.getMessage(), exception);

        if (exception instanceof InvalidInputDataException) {
            return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
        } else {
            return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
