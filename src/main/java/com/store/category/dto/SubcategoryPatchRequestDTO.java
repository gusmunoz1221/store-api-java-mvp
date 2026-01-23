package com.store.category.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubcategoryPatchRequestDTO {
    @Size(min = 3, max = 100,
            message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @Size(max = 255,
            message = "La descripción no puede superar los 255 caracteres")
    private String description;
}
