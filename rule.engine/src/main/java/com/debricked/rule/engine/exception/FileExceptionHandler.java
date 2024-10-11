package com.debricked.rule.engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Centralize exception Handler for DependencyController
 */
@ControllerAdvice
public class FileExceptionHandler {

    /**
     * Exception Handler to handle validation exception while processing file.
     *
     * @param ex Validation Exception thrown by File Processor
     * @return Response Entity with validation error message and status code as 400
     */
    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<String> handleFileValidationException(FileValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    /**
     * Exception Handler to handle generic exception while processing file.
     *
     * @param ex Exception thrown by File Processor
     * @return Response Entity with  error message and status code as 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error occurred while processing file : " + ex.getMessage());
    }

}
