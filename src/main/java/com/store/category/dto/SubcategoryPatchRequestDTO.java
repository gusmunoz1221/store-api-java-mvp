package com.store.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubcategoryPatchRequestDTO {

    @Schema(description = "Nuevo nombre (opcional)", example = "Yerbas Premium")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @Schema(description = "Nueva descripción (opcional)", example = "Seleccion especial de yerbas estacionadas")
    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String description;
}
