package org.example.readingservice.exception.custom;

import org.example.readingservice.exception.CustomException;
/**
 * Exception thrown when a reading is submitted less than a month ago.
 */
public class TooRecentReadingException extends CustomException {
    /**
     * Constructs a new TooRecentReadingException with the specified detail message.
     * @param message the detail message
     */
    public TooRecentReadingException(String message) {
        super(message);
    }
}
