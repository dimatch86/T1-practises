package org.example.readingservice.exception.custom;

import org.example.readingservice.exception.CustomException;

public class BlackListTokenException extends CustomException {

    public BlackListTokenException(String message) {
        super(message);
    }
}
