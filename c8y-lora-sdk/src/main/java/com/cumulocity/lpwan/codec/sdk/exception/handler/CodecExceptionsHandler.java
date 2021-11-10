package com.cumulocity.lpwan.codec.sdk.exception.handler;

import com.cumulocity.lpwan.codec.sdk.exception.DecoderException;
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
