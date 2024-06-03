package com.t1.jwt_auth_app.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель данных для представления пользователя")
public class UserResponseDto {

    @Schema(description = "Имя пользователя", example = "Ivan Ivanov")
    private String name;
    @Schema(description = "Пользовательский email", example = "Dimatch86@mail.ru")
    private String email;

    @Schema(description = "Дата регистрации пользователя", example = "2024-02-23 08:57:50")
    private String registrationDate;
}
