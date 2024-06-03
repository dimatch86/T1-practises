package com.t1.jwt_auth_app.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Модель данных для вывода ошибки")
public class ErrorResponse {

    @Schema(description = "Сообщение об ошибки")
    private String error;
    @Schema(description = "Метка времени возникновения ошибки")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
