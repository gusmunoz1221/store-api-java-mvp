package com.store.product.entity;

import com.store.category.entity.SubcategoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private BigDecimal price;
    private String description;
    private String url;

    @NotNull
    @Column(nullable = false)
    private int stock;

    // PRODUCTO pertenece a UNA subcategoría
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubcategoryEntity subcategory;
}
