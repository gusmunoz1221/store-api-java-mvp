package com.store.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String url;
    private Integer stock;
    private Long subcategoryId;
    private String subcategoryName;
    private String categoryName;
}
