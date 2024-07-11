package org.example.readingservice.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.readingservice.dto.request.ReadingTypeDto;
import org.example.readingservice.dto.response.ResponseDto;
import org.example.readingservice.in.controller.swagger.SwaggerReadingTypeController;
import org.example.readingservice.mapper.mapstruct.ReadingTypeMapper;
import org.example.readingservice.model.reading.ReadingType;
import org.example.readingservice.service.ReadingTypeService;
import org.example.readingservice.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reading-type")
@RequiredArgsConstructor
public class ReadingTypeController implements SwaggerReadingTypeController {

    private final ReadingTypeMapper readingTypeMapper;
    private final ReadingTypeService readingTypeService;

    /**
     * Endpoint for adding a new reading type.
     * @param readingTypeDto the information of the reading type to be added
     * @return a ResponseEntity with a success message
     */
    @PostMapping("/add")
    @Override
    public ResponseEntity<ResponseDto<?>> addNewReadingType(@RequestBody @Valid ReadingTypeDto readingTypeDto) {
        ReadingType readingType = readingTypeMapper
                .readingTypeDtoToReadingType(readingTypeDto);
        readingTypeService.addNewReadingType(readingType);
        return ResponseEntity.ok(ResponseUtil.okResponse("Новый тип показаний сохранен"));
    }

    @GetMapping("/types")
    public ResponseEntity<ResponseDto<List<String>>> getTypes() {
        return ResponseEntity.ok(ResponseUtil
                .okResponseWithData(readingTypeService.getAvailableReadingTypes()));
    }
}
