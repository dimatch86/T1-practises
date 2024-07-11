package org.example.readingservice.service;

import org.example.readingservice.model.reading.ReadingType;
import org.example.readingservice.repository.ReadingTypeRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ReadingTypeServiceTest {

    private final ReadingTypeRepositoryImpl readingTypeRepository = mock(ReadingTypeRepositoryImpl.class);
    private final ReadingTypeServiceImpl readingService = new ReadingTypeServiceImpl(readingTypeRepository);

    @Test
    void addNewReadingType_whenAddNotExistingReadingType_thenCallsSaveNewReadingType() {

        ReadingType notExistingReadingType = new ReadingType("ГАЗ");
        when(readingTypeRepository.findAvailableReadingByType(notExistingReadingType.getType()))
                .thenReturn(Optional.empty());
        readingService.addNewReadingType(notExistingReadingType);
        verify(readingTypeRepository, times(1))
                .saveNewReadingType(notExistingReadingType);
    }
}
