package com.store.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemRequestDTO {
    @NotNull(message = "se requiere el ID del producto")
    private Long productId;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;
}
