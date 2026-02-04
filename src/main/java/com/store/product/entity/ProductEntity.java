package com.store.product.entity;

import com.store.category.entity.SubcategoryEntity;
import com.store.exception.BusinessException;
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

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new BusinessException("Stock insuficiente para el producto: " + this.name);
        }
        this.stock -= quantity;
    }
}
