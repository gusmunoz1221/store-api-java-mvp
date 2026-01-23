package com.store.product.controller;

import com.store.product.dto.ProductResponseDTO;
import com.store.product.service.ProductServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "Products", description = "Operaciones de gestion de productos para clientes")
public class ProductCustomerController {
    private final ProductServiceImp productService;


    // CATALOGO (HOME)
    @Operation(summary = "Catalogo de productos", description = "Lista todos los productos disponibles en stock")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catalogo obtenido correctamente")
    })
    @GetMapping("/catalog")
    public ResponseEntity<Page<ProductResponseDTO>> getCatalog(
            @PageableDefault(size = 10, sort = "name") Pageable pageable){
        return ResponseEntity.ok(productService.listInStock(pageable));
    }


    // BUSCADOR
    @Operation(summary = "Buscar productos", description = "Busca productos disponibles por nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
            @Parameter(description = "Nombre o texto a buscar", example = "mate calabaza")
            @RequestParam String name,
            @PageableDefault(size = 10, sort= "name") Pageable pageable){
        return ResponseEntity.ok(productService.searchAvailableProducts(name,pageable));
    }


    // DETALLE DEL PRODUCTO
    @Operation(summary = "Detalle del producto", description = "Obtiene la información de un producto por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(
            @Parameter(description = "ID del producto", example = "101")
            @PathVariable Long id){
        return ResponseEntity.ok(productService.findById(id));
    }


    // FILTRA POR CATEGORÍA
    @Operation(summary = "Filtrar productos por categoría", description = "Devuelve los productos que pertenecen a una categoría específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos filtrados correctamente")
    })
    @GetMapping("/filter/category/{id}")
    public ResponseEntity<Page<ProductResponseDTO>> filterByCategory(
            @Parameter(description = "ID de la categoría", example = "5")
            @PathVariable Long id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(productService.filterProductsByCategory(id, pageable));
    }


    // FILTRA POR RANGO DE PRECIO
    @Operation(summary = "Filtrar productos por precio", description = "Devuelve los productos cuyo precio está dentro de un rango")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos filtrados por precio correctamente")
    })
    @GetMapping("/filter/price")
    public ResponseEntity<Page<ProductResponseDTO>> filterByPrice(
            @Parameter(description = "Precio mínimo", example = "100.00") @RequestParam BigDecimal min,
            @Parameter(description = "Precio máximo", example = "500.00") @RequestParam BigDecimal max,
            @PageableDefault(size = 10, sort = "price") Pageable pageable) {
        return ResponseEntity.ok(productService.filterByPriceRange(min, max, pageable));
    }
}
