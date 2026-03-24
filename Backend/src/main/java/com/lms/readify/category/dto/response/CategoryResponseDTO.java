package com.lms.readify.category.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponseDTO {

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
