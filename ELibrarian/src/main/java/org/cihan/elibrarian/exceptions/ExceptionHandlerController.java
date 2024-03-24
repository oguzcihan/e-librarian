package org.cihan.elibrarian.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> globalExceptionHandler(GenException ex, WebRequest request) {

        ErrorMessage message = ErrorMessage.builder()
                .statusCode(ex.getStatusCode())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
        return new ResponseEntity<>(message, HttpStatus.valueOf(ex.getStatusCode()));
    }
}
