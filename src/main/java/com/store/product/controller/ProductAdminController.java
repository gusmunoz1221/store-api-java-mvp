package com.store.product.controller;

import com.store.product.dto.ProductPatchRequestDTO;
import com.store.product.dto.ProductRequestDTO;
import com.store.product.dto.ProductResponseDTO;
import com.store.product.service.ProductServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Tag(name = "ADMIN - Productos",
        description = "Gestión administrativa de productos: creación, edición, eliminación, búsqueda y reportes")
public class ProductAdminController {
    private final ProductServiceImp productService;

    @Operation(summary = "Crear producto",
            description = "Crea un nuevo producto y lo asocia a una subcategoría existente. " + "Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "El producto ya existe")})
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO request) {
       return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @Operation(summary = "Actualizar producto",
            description = "Actualiza parcialmente un producto existente. " + "Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto por nombre duplicado")})
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id,
                                                     @RequestBody ProductPatchRequestDTO request) {
        return ResponseEntity.ok((productService.updateProduct(request, id)));
    }

    @Operation(summary = "Eliminar producto",
            description = "Elimina un producto del sistema. " + "Endpoint exclusivo para administradores.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // TABLERO PRINCIPAL -> DASHBOARD
    @Operation(summary = "Listado de productos (dashboard)",
            description = "Obtiene el listado paginado de todos los productos para el panel administrativo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")})
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllForAdmin(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
         return ResponseEntity.ok(productService.listProducts(pageable));
    }

    // REPORTE DE STOCK CRÍTICO
    @Operation(summary = "Productos sin stock",
            description = "Obtiene el listado paginado de productos con stock crítico o sin stock.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente")})
    @GetMapping("/out-of-stock")
    public ResponseEntity<Page<ProductResponseDTO>> getOutOfStock(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(productService.listOutOfStock(pageable));
    }

    @Operation(summary = "Buscar productos",
            description = "Busca productos por nombre dentro del panel administrativo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultados obtenidos correctamente")})
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchForAdmin(@RequestParam String name,
                                                                   @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(productService.searchProducts(name, pageable));
    }
}
