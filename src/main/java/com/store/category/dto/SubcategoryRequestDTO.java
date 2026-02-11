package com.store.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubcategoryRequestDTO {

    @Schema(description = "Nombre de la subcategoria", example = "Yerbas Organicas")
    @NotBlank(message = "El nombre de la subcategoría es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @Schema(description = "Descripcion detallada", example = "Yerbas cultivadas sin agroquimicos")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String description;
}