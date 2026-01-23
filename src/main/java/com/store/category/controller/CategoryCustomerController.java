package com.store.category.controller;

import com.store.category.dto.CategoryResponseDTO;
import com.store.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Operaciones de gestion de categorías visibles para clientes")
public class CategoryCustomerController {
    private final CategoryService categoryService;

    @Operation(summary = "Listar categorías", description = "Devuelve todas las categorías disponibles")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Categorías obtenidas correctamente")})
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.listCategories());
    }

    @Operation(summary = "Listar categorías con subcategorías",
            description = "Devuelve todas las categorías junto con sus subcategorías")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Categorías con subcategorías obtenidas correctamente")})
    @GetMapping("/sub")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategoriesWithSub() {
        return ResponseEntity.ok(categoryService.listCategoriesWithSubcategories());
    }


    // OBTENER CATEGORÍA POR ID
    @Operation(summary = "Obtener categoría por ID", description = "Devuelve una categoría específica según su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.listCategoryById(id));
    }
}
