package com.store.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryRequestDTO {

    @Schema(description = "Nombre de la categoria", example = "Electronica")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 5, max = 100, message = "El nombre debe tener entre 5 y 100 caracteres")
    private String name;

    @Schema(description = "Breve descripción", example = "Gadgets y dispositivos electrónicos")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;
}