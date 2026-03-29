package com.server.lms.category.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponse {

    private String id;

    private String name;

    private String code;

    private String description;

    private Integer displayOrder;

    private Boolean isActive;

    private String parentCategoryId;

    private String parentCategoryName;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
