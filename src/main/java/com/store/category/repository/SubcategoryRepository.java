package com.store.category.repository;

import com.store.category.entity.SubcategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity,Long> {
    List<SubcategoryEntity> findByCategory_Id(Long categoryId);
    boolean existsByName(String name);
}
