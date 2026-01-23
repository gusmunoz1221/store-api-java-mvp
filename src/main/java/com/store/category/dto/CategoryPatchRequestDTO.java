package com.store.category.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryPatchRequestDTO {
    private String name;
    private String description;
}
