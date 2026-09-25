package com.DanielNavia.melody_generator.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(

        @Schema(
                description = "Nombre de usuario",
                example = "daniel"
        )
        @NotBlank
        @Size(min = 3, max = 30)
        String username,

        @Schema(
                description = "Dirección de correo electrónico",
                example = "daniel@test.com"
        )
        @NotBlank
        @Email
        String email,

        @Schema(
                description = "Contraseña del usuario",
                example = "12345678"
        )
        @NotBlank
        @Size(min = 8, max = 100)
        String password
) {
}