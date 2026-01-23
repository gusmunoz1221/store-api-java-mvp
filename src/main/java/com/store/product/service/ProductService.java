package com.store.product.service;

import com.store.product.dto.ProductPatchRequestDTO;
import com.store.product.dto.ProductRequestDTO;
import com.store.product.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface ProductService {
    // CREAR PRODUCTO
    ProductResponseDTO createProduct(ProductRequestDTO product);

    // ACTUALIZAR PRODUCTO
    ProductResponseDTO updateProduct(ProductPatchRequestDTO product, Long productId);

    // ELIMINAR PRODUCTO
    void deleteProduct(Long id);

    //LISTAR PRODCUTO POR PAGINACION
    Page<ProductResponseDTO> listProducts(Pageable pageable);

    // FILTRAR POR CATEGORIA
    Page<ProductResponseDTO> filterBySubcategory(Long subcategoryId, Pageable pageable);

    // FILTRAR POR CATEGORIA (a través de la subcategoría)
    Page<ProductResponseDTO> filterProductsByCategory(Long categoryId, Pageable pageable);

    // FILTRAR POR PRECIOS
    Page<ProductResponseDTO> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // FILTRAR POR MAYOR QUE
    Page<ProductResponseDTO> listInStock(Pageable pageable);

    // LISTAR CON PRODUCTOS MAYOR QUE CERO
    Page<ProductResponseDTO> listOutOfStock(Pageable pageable);

    // ----BUSCARDOR PARA EL ADMIN------
    Page<ProductResponseDTO> searchProducts(String name, Pageable pageable);

    // ----BUSCADOR PARA EL USER----
    Page<ProductResponseDTO> searchAvailableProducts(String name, Pageable pageable);
}
