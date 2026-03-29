package com.server.lms.category.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryTreeResponse {
    private String id;

    private String name;

    private String code;

    private String description;

    private Integer displayOrder;

    private Boolean isActive;

    private String parentCategoryId;

    private String parentCategoryName;

    private List<CategoryTreeResponse> subCategories;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
