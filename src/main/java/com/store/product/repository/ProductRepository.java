package com.store.product.repository;

import com.store.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {

    boolean existsByName(String name);

    // por subcategoría
    Page<ProductEntity> findBySubcategoryId(Long subcategoryId, Pageable pageable);

    // por categoría (de subcategoría)
    Page<ProductEntity> findBySubcategoryCategoryId(Long categoryId, Pageable pageable);


    // por rango de precios
    Page<ProductEntity> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    // con stock mayor que
    Page<ProductEntity> findByStockGreaterThan(int stock, Pageable pageable);

    // sin stock
    Page<ProductEntity> findByStockEquals(int stock, Pageable pageable);

    // para ADMIN
    @Query(value = "SELECT * FROM products p WHERE p.name ~* :regex", nativeQuery = true)
    Page<ProductEntity> searchByNameRegexAny(@Param("regex") String regex, Pageable pageable);

    // para USER
    @Query(value = "SELECT * FROM products p WHERE p.name ~* :regex AND p.stock > 0", nativeQuery = true)
    Page<ProductEntity> searchByNameRegexAvailable(@Param("regex") String regex, Pageable pageable);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.stock = p.stock - :qty WHERE p.id = :id AND p.stock >= :qty")
    int decreaseStock(@Param("id") Long id, @Param("qty") int qty);
}
