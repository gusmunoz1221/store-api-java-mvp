package com.store.category.service;

import com.store.category.dto.*;

import java.util.List;

public interface CategoryService {

    // --- Category ---
    // CREAR CATEGORIA
    CategoryResponseDTO createCategory(CategoryRequestDTO request);

    // ACTUALIZAR CATEGORIA
    CategoryResponseDTO updateCategory(Long id, CategoryPatchRequestDTO request);

    // ELIMINAR CATEGORIA
    void deleteCategory(Long id);

    // LISTA LAS CATEGORIAS
    List<CategoryResponseDTO> listCategories();

    // LISTA LAS CATEGORIAS CON SUB CATEGORIAS
    List<CategoryResponseDTO> listCategoriesWithSubcategories();

    // FILTRAR POR SUBCATEGORIA
    CategoryResponseDTO listCategoryById(Long subcategoryId);

    // -----subcategory----

    // LISTA CATEGORIAS DADO UN ID
    List<SubcategorySimpleDTO> listSubcategoriesByCategoryId(Long categoryId);

    // CREAR SUBCATEGORIA
    SubcategorySimpleDTO createSubcategory(Long categoryId, SubcategoryRequestDTO request);

    // ACTUALIZAR SUBCATEGORIA
    SubcategorySimpleDTO updateSubcategory(Long id, SubcategoryPatchRequestDTO request);

    // ELIMINAR SUBCATEGORIA
    void deleteSubcategory(Long id);
}
