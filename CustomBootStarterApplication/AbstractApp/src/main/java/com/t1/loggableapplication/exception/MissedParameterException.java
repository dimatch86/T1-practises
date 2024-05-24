package com.t1.loggableapplication.exception;

public class MissedParameterException extends RuntimeException {
    public MissedParameterException(String message) {
        super(message);
    }
}
