package com.mindfire.backend.exception;

// Custom exception for when token is expired or already used.
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
