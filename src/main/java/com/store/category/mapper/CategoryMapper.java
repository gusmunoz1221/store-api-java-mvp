package com.store.category.mapper;

import com.store.category.dto.*;
import com.store.category.entity.CategoryEntity;
import com.store.category.entity.SubcategoryEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
//  --------CATEGORIAS----
    public CategoryResponseDTO toCategoryResponse(CategoryEntity entity) {
        if (entity == null) return null;

        List<SubcategorySimpleDTO> subcategoriesDto =
                (entity.getSubcategories() != null)
                        ? entity.getSubcategories().stream()
                        .map(this::toSubcategorySimpleDto)
                        .collect(Collectors.toList())
                        : Collections.emptyList();

        return CategoryResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .subcategories(subcategoriesDto)
                .build();
    }
    public CategoryEntity toCategoryEntity(CategoryRequestDTO request) {
        if (request == null) return null;

        CategoryEntity entity = new CategoryEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public void updateCategoryFromDto(CategoryPatchRequestDTO request, CategoryEntity entity) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
    }

    // --------SUBCATEGORIAS---------

    public SubcategorySimpleDTO toSubcategorySimpleDto(SubcategoryEntity entity) {
        if (entity == null) return null;

        return SubcategorySimpleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public SubcategoryEntity toSubcategoryEntity(SubcategoryRequestDTO request) {
        if (request == null) return null;

        SubcategoryEntity entity = new SubcategoryEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public void updateSubcategoryFromDto(SubcategoryPatchRequestDTO request, SubcategoryEntity entity) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
    }
}

