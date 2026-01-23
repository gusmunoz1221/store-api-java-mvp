package com.store.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductRequestDTO {
    @NotBlank(message = "se requiere el nombre del producto")
    private String name;
    @NotNull(message = "se requiere el precion el producto")
    private BigDecimal price;
    private String description;
    @NotBlank(message = "se requiere la URL de la imagen del prodcuto")
    private String url;
    @NotNull(message = "se requiere el stock del producto")
    @Min(value = 0, message = "el stock no puede ser negativo")
    private Integer stock;
    @NotNull(message = "se requiere el ID de la subcategoria")
    private Long subcategoryId;
}
