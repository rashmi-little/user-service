package com.mindfire.backend.exception;

//custom exception when the old password and new password are same during reset
public class SamePasswordException extends RuntimeException {
    public SamePasswordException(String message) {
        super(message);
    }
}
