package org.example.readingservice.in.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.readingservice.dto.request.ReadingDto;
import org.example.readingservice.dto.response.ReadingListResponseDto;
import org.example.readingservice.dto.response.ReadingResponseDto;
import org.example.readingservice.dto.response.ResponseDto;
import org.example.readingservice.security.AppUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Контроллер для работы с показаниями")
public interface SwaggerReadingController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Показания успешно переданы",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Не поддерживаемый тип показаний",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content)})
    @Operation(summary = "Передача показаний")
    ResponseEntity<ResponseDto<?>> sendReading(@RequestBody @Valid ReadingDto readingDto, AppUserDetails userDetails);

    @Operation(summary = "Вывод списка актуальных показаний. Пользователь с ролью \"USER\" получает список только своих покаказаний")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReadingListResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content)})
    ResponseEntity<ResponseDto<List<ReadingResponseDto>>> getActualReadings(AppUserDetails userDetails);

    @Operation(summary = "Вывод истории показаний. Пользователь с ролью \"USER\" получает список только своих покаказаний")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReadingListResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content)})
    ResponseEntity<ResponseDto<List<ReadingResponseDto>>> getHistoryOfReadings(AppUserDetails userDetails);

    @Operation(summary = "Вывод показаний за определенный месяц. Номер месяца указывается в параметре запроса. " +
            "Пользователь с ролью \"USER\" получает список только своих покаказаний")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReadingListResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос, отсутствует параметр",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content)})
    ResponseEntity<ResponseDto<List<ReadingResponseDto>>> getReadingsByMonth(@RequestParam(value = "monthNumber", required = false)
                                                                             @Parameter(name = "monthNumber", description = "month number parameter", example = "2") Integer monthNumber,
                                                                             AppUserDetails userDetails);
}
