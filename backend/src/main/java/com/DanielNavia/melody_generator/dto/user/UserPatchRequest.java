package com.DanielNavia.melody_generator.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserPatchRequest(

        @Schema(
                description = "Nuevo nombre de usuario",
                example = "daniel2",
                nullable = true
        )
        @Size(min = 3, max = 30)
        String username,

        @Schema(
                description = "Nueva dirección de correo electrónico",
                example = "daniel2@test.com",
                nullable = true
        )
        @Email
        String email,

        @Schema(
                description = "Nueva contraseña",
                example = "12345678",
                nullable = true
        )
        @Size(min = 8, max = 100)
        String password
) {
}