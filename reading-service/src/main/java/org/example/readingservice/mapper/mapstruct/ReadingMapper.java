package org.example.readingservice.mapper.mapstruct;

import org.example.readingservice.dto.request.ReadingDto;
import org.example.readingservice.dto.response.ReadingResponseDto;
import org.example.readingservice.model.reading.Reading;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

/**
 * An interface that serves as a mapper for converting data transfer objects (DTO) to
 * domain objects and vice versa.
 * Apply decorators from ReadingMapperDelegate for additional mapping behaviors.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(ReadingMapperDelegate.class)
public interface ReadingMapper {

    /**
     * Converts a ReadingDto object to a Reading object.
     * @param readingDto the input ReadingDto object
     * @return a corresponding Reading object
     */
    Reading readingDtoToReading(ReadingDto readingDto, UUID personalAccount);

    /**
     * Converts a list of Reading objects to a list of ReadingResponse objects.
     * @param readingList the input list of Reading objects
     * @return a corresponding list of ReadingResponse objects
     */
    List<ReadingResponseDto> readingListToResponseList(List<Reading> readingList);
}
