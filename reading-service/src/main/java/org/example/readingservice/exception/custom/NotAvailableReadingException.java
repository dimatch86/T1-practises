package org.example.readingservice.exception.custom;

import org.example.readingservice.exception.CustomException;
/**
 * NotAvailableReadingException class represents an exception that is thrown
 * when the reading material is not available.
 */
public class NotAvailableReadingException extends CustomException {
    /**
     * Constructs a new NotAvailableReadingException with the specified detail message.
     * @param message the detail message
     */
    public NotAvailableReadingException(String message) {
        super(message);
    }
}
