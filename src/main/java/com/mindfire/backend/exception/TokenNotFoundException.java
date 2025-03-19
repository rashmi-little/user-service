package com.mindfire.backend.exception;

// Custom exception for when token is not found.
public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
