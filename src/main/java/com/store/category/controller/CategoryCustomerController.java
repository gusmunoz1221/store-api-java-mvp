package com.store.category.controller;

import com.store.category.dto.CategoryResponseDTO;
import com.store.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Categories", description = "Catalogo público de categorías")
public class CategoryCustomerController {

    private final CategoryService categoryService;

    @Operation(summary = "Ver todas las categorías", description = "Lista plana de categorías disponibles")
    @ApiResponse(responseCode = "200", description = "Exito",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))))
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.listCategories());
    }

    @Operation(summary = "Ver arbol de categorias", description = "Lista categorías incluyendo sus hijos (subcategorias)")
    @ApiResponse(responseCode = "200", description = "Éxito",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class))))
    @GetMapping("/sub")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategoriesWithSub() {
        return ResponseEntity.ok(categoryService.listCategoriesWithSubcategories());
    }

    @Operation(summary = "Buscar categoría", description = "Obtiene detalle de una categoría por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encontrada",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe categoría con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "ID numérico de la categoría", example = "5")
            @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.listCategoryById(id));
    }
}