package com.store.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryPatchRequestDTO {

    @Schema(description = "Nuevo nombre (opcional)", example = "Electronica y Hogar")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @Schema(description = "Nueva descripción (opcional)", example = "Nueva descripción actualizada")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;
}
