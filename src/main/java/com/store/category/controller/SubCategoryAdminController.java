package com.store.category.controller;

import com.store.category.dto.SubcategoryPatchRequestDTO;
import com.store.category.dto.SubcategoryRequestDTO;
import com.store.category.dto.SubcategorySimpleDTO;
import com.store.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/subcategories")
@RequiredArgsConstructor
@Tag(name = "ADMIN - Subcategorías",
        description = "Gestion de subcategorías (Creación dependiente de categorías padre)")
public class SubCategoryAdminController {

    private final CategoryService categoryService;

    // CREAR SUBCATEGORIA
    @Operation(summary = "Crear subcategoria",
            description = "Crea una subcategoria y la vincula inmediatamente a una Categoria Padre existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Subcategoría creada exitosamente",
                    content = @Content(schema = @Schema(implementation = SubcategorySimpleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos (nombre vacío o muy corto)"),
            @ApiResponse(responseCode = "404", description = "La Categoría Padre especificada no existe"),
            @ApiResponse(responseCode = "409", description = "Ya existe una subcategoría con ese nombre en esta categoría")
    })
    @PostMapping("/{categoryId}")
    public ResponseEntity<SubcategorySimpleDTO> create(
            @Parameter(description = "ID de la Categoría Padre a la que pertenecera", example = "10", required = true)
            @PathVariable Long categoryId,
            @Valid @RequestBody SubcategoryRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createSubcategory(categoryId, request));
    }

    // ACTUALIZAR SUBCATEGORIA
    @Operation(summary = "Actualizar subcategoría parcialmente",
            description = "Permite modificar nombre o descripcion sin enviar todo el objeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizacion correcta",
                    content = @Content(schema = @Schema(implementation = SubcategorySimpleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos (longitud incorrecta)"),
            @ApiResponse(responseCode = "404", description = "ID de subcategoría no encontrado")
    })
    @PatchMapping("/{id}") //
    public ResponseEntity<SubcategorySimpleDTO> update(
            @Parameter(description = "ID de la subcategoría a editar", example = "55")
            @PathVariable Long id,
            @Valid @RequestBody SubcategoryPatchRequestDTO request) {

        return ResponseEntity.ok(categoryService.updateSubcategory(id, request));
    }

    // ELIMINAR SUBCATEGORIA
    @Operation(summary = "Eliminar subcategoria",
            description = "Elimina el registro de la subcategoria. Requiere que no tenga productos activos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminacion exitosa"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar: tiene productos asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la subcategoría a eliminar", example = "55")
            @PathVariable Long id) {

        categoryService.deleteSubcategory(id);
        return ResponseEntity.noContent().build();
    }
}