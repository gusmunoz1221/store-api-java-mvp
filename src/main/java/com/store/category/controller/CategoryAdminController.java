package com.store.category.controller;

import com.store.category.dto.CategoryPatchRequestDTO;
import com.store.category.dto.CategoryRequestDTO;
import com.store.category.dto.CategoryResponseDTO;
import com.store.category.service.CategoryServiceImp;
import io.swagger.v3.oas.annotations.Operation;
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
        description = "Endpoints de administración para crear, actualizar y eliminar categorías")
public class CategoryAdminController {
    private final CategoryServiceImp categoryService;

    @Operation(summary = "Crear categoría",
            description = "Crea una nueva categoría. Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "La categoría ya existe")})
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @Operation(summary = "Actualizar categoría",
            description = "Actualiza parcialmente una categoría existente. Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre")})
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id,
                                                      @RequestBody CategoryPatchRequestDTO request){
        return ResponseEntity.ok(categoryService.updateCategory(id,request));
    }

    @Operation(summary = "Eliminar categoría",
            description = "Elimina una categoría si no tiene subcategorías asociadas ni productos. Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "La categoría tiene subcategorías asociadas o productos")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
