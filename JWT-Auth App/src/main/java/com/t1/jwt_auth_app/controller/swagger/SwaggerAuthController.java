package com.t1.jwt_auth_app.controller.swagger;

import com.t1.jwt_auth_app.model.request.AuthRequestDto;
import com.t1.jwt_auth_app.model.request.UserDto;
import com.t1.jwt_auth_app.model.response.AuthResponseDto;
import com.t1.jwt_auth_app.model.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Контроллер для авторизации пользователя")
public interface SwaggerAuthController {

    @Operation(summary = "Зарегистрировать новый аккаунт")
    @ApiResponse(responseCode = "201", description = "Аккаунт успешно зарегистрирован",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserResponseDto.class, type = "array")))
    @ApiResponse(responseCode = "400", description = "Некорректный запрос. Проверьте параметры запроса.", content = @Content)
    ResponseEntity<UserResponseDto> registerNewUser(@RequestBody @Valid UserDto userDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Не верный пароль",
                    content = @Content)})
    @Operation(summary = "Вход пользователя по его email и паролю")
    ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto);


}
