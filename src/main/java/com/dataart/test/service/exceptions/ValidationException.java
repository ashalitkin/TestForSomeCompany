package com.dataart.test.service.exceptions;

/**
 * Created by andrey on 24/05/2014.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
