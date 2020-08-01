package com.epam.esm.bahlei.restbasic.service.exception;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException() {
        super();
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }

    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}