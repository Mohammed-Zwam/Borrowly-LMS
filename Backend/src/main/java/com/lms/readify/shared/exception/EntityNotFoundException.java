package com.lms.readify.shared.exception;

public class EntityNotFoundException extends RuntimeException  {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
