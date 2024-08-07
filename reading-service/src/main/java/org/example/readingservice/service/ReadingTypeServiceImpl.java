package org.example.readingservice.service;

import lombok.RequiredArgsConstructor;
//import org.example.loggingstarter.aop.Loggable;
import org.example.readingservice.exception.custom.ReadingTypeAlreadyExistsException;
import org.example.readingservice.model.reading.ReadingType;
import org.example.readingservice.repository.ReadingTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Loggable
@RequiredArgsConstructor
public class ReadingTypeServiceImpl implements ReadingTypeService {

    private final ReadingTypeRepository readingTypeRepository;
        /**
         * Method to add a new reading type.
         * @param readingType the reading type to be added
         * @throws ReadingTypeAlreadyExistsException if the reading type already exists
         */

        @Override
        public void addNewReadingType(ReadingType readingType) {
            readingTypeRepository.saveNewReadingType(readingType);
        }

    @Override
    public List<String> getAvailableReadingTypes() {
        return readingTypeRepository.findAvailableReadings();
    }
}
