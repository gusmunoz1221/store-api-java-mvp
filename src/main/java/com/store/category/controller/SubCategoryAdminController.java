package com.store.category.controller;

import com.store.category.dto.SubcategoryPatchRequestDTO;
import com.store.category.dto.SubcategoryRequestDTO;
import com.store.category.dto.SubcategorySimpleDTO;
import com.store.category.service.CategoryService;
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
@RequestMapping("/admin/subcategories")
@RequiredArgsConstructor
@Tag(name = "ADMIN - Subcategorías",
        description = "Endpoints administrativos para crear, actualizar y eliminar subcategorías")
public class SubCategoryAdminController {
    private final CategoryService categoryService;

    @Operation(summary = "Crear subcategoría",
            description = "Crea una nueva subcategoría asociada a una categoría existente. " +
                    "Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Subcategoría creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "La subcategoría ya existe")})
    @PostMapping("/{categoryId}")
    public ResponseEntity<SubcategorySimpleDTO> create(@PathVariable Long categoryId,
                                                        @Valid @RequestBody SubcategoryRequestDTO request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createSubcategory(categoryId,request));
    }

    @Operation(summary = "Actualizar subcategoría",
            description = "Actualiza parcialmente una subcategoría existente. " + "Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subcategoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "La subcategoría ya existe")})
    @PutMapping("/{id}")
    public ResponseEntity<SubcategorySimpleDTO> update(@PathVariable Long id,
                                                       @RequestBody SubcategoryPatchRequestDTO request){
        return ResponseEntity.ok(categoryService.updateSubcategory(id,request));
    }

    @Operation(summary = "Eliminar subcategoría",
            description = "Elimina una subcategoría si no tiene productos asociados. " + "Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subcategoría eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "La subcategoría tiene productos asociados")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.deleteSubcategory(id);
        return ResponseEntity.noContent().build();
    }

}
