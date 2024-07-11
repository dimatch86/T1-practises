package org.example.readingservice.exception.custom;

import org.example.readingservice.exception.CustomException;
/**
 * Exception thrown when a required parameter is missing.
 */
public class ParameterMissingException extends CustomException {
    /**
     * Constructs a new ParameterMissingException with the specified detail message.
     * @param message the detail message
     */
    public ParameterMissingException(String message) {
        super(message);
    }
}
