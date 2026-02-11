package com.store.category.controller;

import com.store.category.dto.CategoryPatchRequestDTO;
import com.store.category.dto.CategoryRequestDTO;
import com.store.category.dto.CategoryResponseDTO;
import com.store.category.service.CategoryService;
import com.store.category.service.CategoryServiceImp;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Tag(name = "ADMIN - Categorías",
        description = "Endpoints protegidos para gestión de catálogo")
public class CategoryAdminController {

    // CORRECCION: Inyectamos la Interfaz, no la implementación concreta
    private final CategoryService categoryService;

    @Operation(summary = "Crear categoría",
            description = "Registra una nueva categoría en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación (ej. nombre duplicado o formato inválido)"),
            @ApiResponse(responseCode = "409", description = "Conflicto: La categoría ya existe")
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @Operation(summary = "Actualizar categoría parcialmente",
            description = "Actualiza nombre o descripción. Solo envía los campos que quieras modificar.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualización exitosa",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos (longitud incorrecta)"),
            @ApiResponse(responseCode = "404", description = "ID de categoría no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryPatchRequestDTO request){
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @Operation(summary = "Eliminar categoría",
            description = "Elimina una categoría lógica o físicamente. Falla si tiene productos asociados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminacion exitosa (Sin contenido)"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar: tiene dependencias (productos/subcategorias)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}