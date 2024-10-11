package com.debricked.rule.engine.exception;

/**
 * Custom Exception Class to handle validation
 */
public class FileValidationException extends RuntimeException{

    public FileValidationException(String message) {
        super(message);
    }
}
