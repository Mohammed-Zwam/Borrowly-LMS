package com.lms.readify.category.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryTreeResponseDTO {
    private String id;

    private String name;

    private String code;

    private String description;

    private Integer displayOrder;

    private Boolean isActive;

    private String parentCategoryId;

    private String parentCategoryName;

    private List<CategoryTreeResponseDTO> subCategories;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
