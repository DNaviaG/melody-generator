package com.DanielNavia.melody_generator.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserResponse(

        @Schema(
                description = "Identificador del usuario",
                example = "1"
        )
        Integer id,

        @Schema(
                description = "Nombre de usuario",
                example = "daniel"
        )
        String username,

        @Schema(
                description = "Correo electrónico",
                example = "daniel@test.com"
        )
        String email,

        @Schema(
                description = "Fecha y hora de creación del usuario",
                example = "2026-09-25T20:00:00"
        )
        LocalDateTime createdAt
) {
}