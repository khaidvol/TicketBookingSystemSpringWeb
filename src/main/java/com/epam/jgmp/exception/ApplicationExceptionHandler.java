package com.epam.jgmp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity handleApplicationException(ApplicationException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
