package com.server.lms._shared.exception;

public class EntityNotFoundException extends RuntimeException  {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
