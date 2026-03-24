package com.lms.readify.category.mapper;

import com.lms.readify.category.dto.request.CategoryRequestDTO;
import com.lms.readify.category.dto.response.CategoryResponseDTO;
import com.lms.readify.category.dto.response.CategoryTreeResponseDTO;
import com.lms.readify.category.entity.Category;
import com.lms.readify.category.service.CategoryService;
import com.lms.readify.shared.exception.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class CategoryMapper {

    @Autowired
    @Lazy
    private CategoryService categoryService;


    @Mappings({
            @Mapping(source = "parentCategory.id", target = "parentCategoryId"),
            @Mapping(source = "parentCategory.name", target = "parentCategoryName")
    })
    public abstract CategoryTreeResponseDTO toCategoryTreeDTO(Category entity);


    @Mappings({
            @Mapping(source = "parentCategory.id", target = "parentCategoryId"),
            @Mapping(source = "parentCategory.name", target = "parentCategoryName")
    })
    public abstract CategoryResponseDTO toCategoryDTO(Category entity);


    public abstract List<CategoryResponseDTO> toCategoryDTOs(List<Category> categories);

    public abstract List<CategoryTreeResponseDTO> toCategoryTreeDTOs(List<Category> categories);


    @Mappings({
            @Mapping(ignore = true, target = "createdDate"),
            @Mapping(ignore = true, target = "updatedDate"),
    })
    public abstract Category toEntity(CategoryRequestDTO dto);

    @Mappings({
            @Mapping(ignore = true, target = "createdDate"),
            @Mapping(ignore = true, target = "updatedDate"),
    })


    public abstract Category toEntity(@MappingTarget Category category, CategoryRequestDTO dto);


    @AfterMapping
    void getParentCategory(CategoryRequestDTO dto, @MappingTarget Category entity) {
        if (dto.getParentCategoryId() == null) {
            entity.setParentCategory(null);
        } else {
            Category parent = categoryService.findEntityById(dto.getParentCategoryId(), "Parent category");
            entity.setParentCategory(parent);
        }
    }

}