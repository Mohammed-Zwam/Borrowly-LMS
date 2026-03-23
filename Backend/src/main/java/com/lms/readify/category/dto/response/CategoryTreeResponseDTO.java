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


    @NotBlank(message = "Category name is required")
    private String name;

    @NotBlank(message = "Category code is required")
    private String code;

    @NotBlank(message = "Category description is required")
    @Size(max = 500, message = "Category description must not exceed 500 characters")
    private String description;

    @Min(value = 0, message = "Display Order cannot be less than zero")
    private Integer displayOrder;

    private Boolean isActive;

    private String parentCategoryId;

    private String parentCategoryName;

    private List<CategoryTreeResponseDTO> subCategories;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
