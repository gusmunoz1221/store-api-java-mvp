package com.store.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemRequestDTO {

    @Schema(description = "ID unico del producto a agregar", example = "5")
    @NotNull(message = "Se requiere el ID del producto")
    private Long productId;

    @Schema(description = "Cantidad de unidades", example = "2", minimum = "1")
    @NotNull
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;
}