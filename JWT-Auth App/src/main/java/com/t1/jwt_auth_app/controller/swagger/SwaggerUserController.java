package com.t1.jwt_auth_app.controller.swagger;

import com.t1.jwt_auth_app.model.entity.RoleType;
import com.t1.jwt_auth_app.model.request.UserDto;
import com.t1.jwt_auth_app.model.response.UserResponseDto;
import com.t1.jwt_auth_app.security.AppUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Контроллер для работы с пользователями")
public interface SwaggerUserController {

    @Operation(summary = "Получение информации о текущем пользователе", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content)})
    ResponseEntity<UserResponseDto> getCurrentUser(AppUserDetails userDetails);

    @Operation(summary = "Получение списка всех пользователей", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content)})
    ResponseEntity<List<UserResponseDto>> getUsers();

    @Operation(summary = "Добавление роли пользователю", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для выполнения",
                    content = @Content)})
    ResponseEntity<UserResponseDto> addRole(@Parameter(description = "id of user to be searched") Long id,
                                            @Parameter(description = "type of role to be added to user") RoleType type);

    @Operation(summary = "Удаление роли пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для выполнения",
                    content = @Content)})
    ResponseEntity<UserResponseDto> removeRole(@Parameter(description = "id of user to be searched") Long id,
                                            @Parameter(description = "type of role to be removed to user") RoleType type);

    @Operation(summary = "Обновление пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для выполнения",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)})
    ResponseEntity<UserResponseDto> updateUser(@Parameter(description = "id of user to be searched") Long id, UserDto userDto);

    @Operation(summary = "Удаление пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Статус NO_CONTENT"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для выполнения",
                    content = @Content)})
    ResponseEntity<Void> deleteUser(@Parameter(description = "id of user to be searched") Long id);
}
