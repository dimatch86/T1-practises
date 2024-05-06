package com.t1.runtimetracker.exception;

import com.t1.runtimetracker.aop.annotation.Throw;

@Throw
public class ProductException extends RuntimeException {

    public ProductException(String message) {
        super(message);
    }
}
