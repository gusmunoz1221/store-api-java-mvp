package com.store.category.entity;

import com.store.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "subcategories")
public class SubcategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // MUCHAS subcategorías pertenecen a UNA categoría
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    // UNA subcategoría tiene MUCHOS productos
    @OneToMany(mappedBy = "subcategory")
    private List<ProductEntity> products = new ArrayList<>();
}
