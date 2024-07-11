package org.example.readingservice.service;

import org.example.readingservice.model.reading.ReadingType;

import java.util.List;

public interface ReadingTypeService {

    /**
     * Adds a new reading type.
     * @param readingType the reading type to be added
     */
    void addNewReadingType(ReadingType readingType);

    List<String> getAvailableReadingTypes();
}
