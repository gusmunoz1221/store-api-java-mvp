package com.store.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {

    @Schema(description = "Correo electrónico del usuario", example = "admin@store.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "admin!")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
