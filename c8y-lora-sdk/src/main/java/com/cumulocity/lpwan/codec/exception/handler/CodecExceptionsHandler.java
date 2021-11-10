/*
~ Copyright (c) 2012-2021 Cumulocity GmbH
~ Copyright (c) 2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA,
~ and/or its subsidiaries and/or its affiliates and/or their licensors.
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~     http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" BASIS,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
*/

package com.cumulocity.lpwan.codec.exception.handler;

import com.cumulocity.lpwan.codec.exception.DecoderException;
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

@ControllerAdvice
@Slf4j
public class CodecExceptionsHandler {

    @ExceptionHandler(value = DecoderException.class)
    @ResponseBody
    public ResponseEntity<String> handleException(DecoderException exception) throws Exception {
        log.error(exception.getMessage(), exception);
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    @ResponseBody
    public ResponseEntity<String> handleException(UnsupportedOperationException exception) throws Exception {
        log.error(exception.getMessage(), exception);
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> buildErrorResponse(Exception exception, HttpStatus status) {
        ErrorMessageRepresentation representation = buildErrorMessageRepresentation(exception);
        String errorJson = JSON.defaultJSON().forValue(representation);
        return ResponseEntity.status(status).contentType(MediaType.parseMediaType(ERROR_MESSAGE_TYPE)).body(errorJson);
    }

    private ErrorMessageRepresentation buildErrorMessageRepresentation(Exception exception) {
        ErrorMessageRepresentation representation = new ErrorMessageRepresentation();
        representation.setError("Codec Microservice Error");
        representation.setMessage(exception.getMessage());
        representation.setDetails(buildErrorDetails(exception));
        return representation;
    }

    private ErrorDetails buildErrorDetails(Exception exception) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setExpectionClass(exception.getClass().getCanonicalName());
        errorDetails.setExceptionMessage(exception.getMessage());
        return errorDetails;
    }
}
