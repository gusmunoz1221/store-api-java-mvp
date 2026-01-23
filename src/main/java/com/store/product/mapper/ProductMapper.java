package com.store.product.mapper;

import com.store.category.entity.SubcategoryEntity;
import com.store.product.dto.ProductPatchRequestDTO;
import com.store.product.dto.ProductRequestDTO;
import com.store.product.dto.ProductResponseDTO;
import com.store.product.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity dtoToEntity(ProductRequestDTO dto, SubcategoryEntity subcategory) {
        ProductEntity entity = new ProductEntity();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setUrl(dto.getUrl());
        entity.setStock(dto.getStock());
        entity.setSubcategory(subcategory);

        return entity;
    }

    public ProductResponseDTO entityToDto(ProductEntity entity) {
        return ProductResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .url(entity.getUrl())
                .stock(entity.getStock())
                .subcategoryId(entity.getSubcategory() != null ? entity.getSubcategory().getId() : null)
                .subcategoryName(entity.getSubcategory() != null ? entity.getSubcategory().getName() : null)
                .categoryName(entity.getSubcategory() != null &&
                        entity.getSubcategory().getCategory() != null
                        ? entity.getSubcategory().getCategory().getName()
                        : null)
                .build();
    }

    public void updateEntity(ProductEntity entity, ProductPatchRequestDTO productRequest, SubcategoryEntity subcategory) {
        if (productRequest.getName() != null) entity.setName(productRequest.getName());
        if (productRequest.getPrice() != null) entity.setPrice(productRequest.getPrice());
        if (productRequest.getDescription() != null) entity.setDescription(productRequest.getDescription());
        if (productRequest.getUrl() != null) entity.setUrl(productRequest.getUrl());
        if (productRequest.getStock() != null) entity.setStock(productRequest.getStock());
        if (subcategory != null) entity.setSubcategory(subcategory);
    }
}