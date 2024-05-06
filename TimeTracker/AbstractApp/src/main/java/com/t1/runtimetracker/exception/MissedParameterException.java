package com.t1.runtimetracker.exception;

import com.t1.runtimetracker.aop.annotation.Throw;

@Throw
public class MissedParameterException extends RuntimeException {
    public MissedParameterException(String message) {
        super(message);
    }
}
