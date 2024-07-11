package org.example.readingservice.in.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.readingservice.dto.request.LoginRequestDto;
import org.example.readingservice.dto.request.UserDto;
import org.example.readingservice.dto.response.AuthResponseDto;
import org.example.readingservice.dto.response.ResponseDto;
import org.example.readingservice.dto.response.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Контроллер для авторизации пользователя")
public interface SwaggerAuthController {

    @Operation(summary = "Зарегистрировать новый аккаунт")
    @ApiResponse(responseCode = "200", description = "Аккаунт успешно зарегистрирован",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseDto.class, type = "array")))
    @ApiResponse(responseCode = "400", description = "Некорректный запрос. Проверьте параметры запроса.", content = @Content)
    ResponseEntity<ResponseDto<?>> registerNewUser(@RequestBody @Valid UserDto userDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Не верный пароль",
                    content = @Content)})
    @Operation(summary = "Вход пользователя по его email и паролю")
    ResponseEntity<ResponseDto<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto);

    @Operation(summary = "Получение информации о текущем пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content)})
    ResponseEntity<ResponseDto<?>> getCurrentUserInfo(UserDetails userDetails);
}
