package org.example.readingservice.exception.custom;

import org.example.readingservice.exception.CustomException;
/**
 * Exception thrown when a reading type already exists in a database.
 */
public class ReadingTypeAlreadyExistsException extends CustomException {
    /**
     * Constructs a new ReadingTypeAlreadyExistsException with the specified detail message.
     * @param message the detail message
     */
    public ReadingTypeAlreadyExistsException(String message) {
        super(message);
    }
}
