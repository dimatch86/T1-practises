package com.t1.jwt_auth_app.exception;

public class SelfModificationException extends RuntimeException {
    public SelfModificationException(String message) {
        super(message);
    }
}
