package com.store.category.service;

import com.store.category.dto.*;
import com.store.category.entity.CategoryEntity;
import com.store.category.entity.SubcategoryEntity;
import com.store.category.mapper.CategoryMapper;
import com.store.category.repository.CategoryRepository;
import com.store.category.repository.SubcategoryRepository;
import com.store.exception.BusinessException;
import com.store.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryMapper categoryMapper;

                                     //-----CATEGORIAS------
    /**
     * Crea una nueva categoría a partir del DTO recibido y la persiste en el sistema.
     * @param request DTO con los datos de la categoría a crear
     * @return DTO de la categoría creada
     */
    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        CategoryEntity entity = categoryMapper.toCategoryEntity(request);
        CategoryEntity savedEntity = categoryRepository.save(entity);
        return categoryMapper.toCategoryResponse(savedEntity);
    }

    /**
     * Actualiza parcialmente una categoría existente.
     * <p>Aplica únicamente los campos presentes en el request y valida que
     * el nombre no se encuentre duplicado si se solicita un cambio.
     * @param id identificador de la categoría a actualizar
     * @param request DTO con los campos a modificar
     * @return DTO de la categoría actualizada
     * @throws ResourceNotFoundException si la categoría no existe
     * @throws BusinessException si el nombre ya está en uso
     */
    @Override
    @Transactional
    public CategoryResponseDTO updateCategory( @NotNull(message = "El id de la categoría es obligatorio") Long id,
                                               @Valid CategoryPatchRequestDTO request) {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada ID: " + id));

        if (request.getName() != null &&
                !category.getName().equalsIgnoreCase(request.getName()) &&
                categoryRepository.existsByName(request.getName()))
            throw new BusinessException("La categoría ya existe");

        categoryMapper.updateCategoryFromDto(request, category);

        return categoryMapper.toCategoryResponse(category);
    }

    /**
     * Elimina una categoría existente.
     * <p>No se permite la eliminación si la categoría posee subcategorías
     * asociadas, para preservar la integridad del modelo.
     * @param id identificador de la categoría a eliminar
     * @throws ResourceNotFoundException si la categoría no existe
     * @throws BusinessException si la categoría tiene subcategorías asociadas
     */
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada ID: " + id));

        if (!category.getSubcategories().isEmpty())
            throw new BusinessException("No se puede eliminar una categoría con subcategorías asociadas");

        categoryRepository.delete(category);
    }

    /**
     * Retorna todas las categorías disponibles.
     * @return lista de categorías
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> listCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas las categorías junto con sus subcategorías asociadas.
     * <p>Utiliza una consulta optimizada para evitar problemas de N+1.
     * @return lista de categorías con subcategorías
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> listCategoriesWithSubcategories() {
        return categoryRepository.findAllWithSubcategories().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO listCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada ID: " + id));
    }

                          //    ----SUBCATEGORIAS------
    /**
     * Crea una nueva subcategoría asociada a una categoría existente.
     * @param categoryId identificador de la categoría padre
     * @param request DTO con los datos de la subcategoría
     * @return DTO simple de la subcategoría creada
     * @throws ResourceNotFoundException si la categoría padre no existe
     */
    @Override
    @Transactional
    public SubcategorySimpleDTO createSubcategory(Long categoryId, SubcategoryRequestDTO request) {
        CategoryEntity parent = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("La categoría ID " + categoryId + " no existe"));

        SubcategoryEntity sub = categoryMapper.toSubcategoryEntity(request);

        sub.setCategory(parent);
        parent.getSubcategories().add(sub);

        SubcategoryEntity saved = subcategoryRepository.save(sub);
        return categoryMapper.toSubcategorySimpleDto(saved);
    }

    /**
     * Retorna todas las subcategorías asociadas a una categoría.
     * @param categoryId identificador de la categoría
     * @return lista de subcategorías
     * @throws ResourceNotFoundException si la categoría no existe
     */
    @Override
    @Transactional(readOnly = true)
    public List<SubcategorySimpleDTO> listSubcategoriesByCategoryId(Long categoryId) {
        if(!categoryRepository.existsById(categoryId))
            throw new ResourceNotFoundException("La categoría ID " + categoryId + " no existe");

        return subcategoryRepository.findByCategory_Id(categoryId)
                .stream()
                .map(categoryMapper::toSubcategorySimpleDto)
                .toList();
    }

    /**
     * Actualiza parcialmente una subcategoría existente.
     * <p>Valida que el nombre no se encuentre duplicado si se solicita un cambio.
     * @param id identificador de la subcategoría
     * @param request DTO con los campos a modificar
     * @return DTO simple de la subcategoría actualizada
     * @throws ResourceNotFoundException si la subcategoría no existe
     * @throws BusinessException si el nombre ya está en uso
     */
    @Override
    @Transactional
    public SubcategorySimpleDTO updateSubcategory(
            @NotNull(message = "El id de la subcategoría es obligatorio") Long id,
            @Valid SubcategoryPatchRequestDTO request) {

        SubcategoryEntity subcategory = subcategoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subcategoría no encontrada ID: " + id));

        if (request.getName() != null &&
                !subcategory.getName().equalsIgnoreCase(request.getName()) &&
                subcategoryRepository.existsByName(request.getName()))
            throw new BusinessException("La subcategoría ya existe");


        categoryMapper.updateSubcategoryFromDto(request, subcategory);

        return categoryMapper.toSubcategorySimpleDto(subcategory);
    }

    /**
     * Elimina una subcategoría existente.
     * <p>No se permite la eliminación si la subcategoría posee productos
     * asociados, para evitar inconsistencias en el catálogo.
     * @param id identificador de la subcategoría a eliminar
     * @throws ResourceNotFoundException si la subcategoría no existe
     * @throws BusinessException si la subcategoría tiene productos asociados
     */
    @Override
    @Transactional
    public void deleteSubcategory(Long id) {
        SubcategoryEntity sub = subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada ID: " + id));

        if (!sub.getProducts().isEmpty())
            throw new BusinessException("La subcategoría tiene productos asociados y no puede eliminarse");

        CategoryEntity parent = sub.getCategory();
        parent.getSubcategories().remove(sub);

        subcategoryRepository.delete(sub);
    }
}
