package com.t1.jwt_auth_app.exception;

public class BlackListTokenException extends RuntimeException {

    public BlackListTokenException(String message) {
        super(message);
    }
}
