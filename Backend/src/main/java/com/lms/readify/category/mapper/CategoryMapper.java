package com.lms.readify.category.mapper;

import com.lms.readify.category.dto.request.CategoryRequestDTO;
import com.lms.readify.category.dto.response.CategoryResponseDTO;
import com.lms.readify.category.dto.response.CategoryTreeResponseDTO;
import com.lms.readify.category.entity.Category;
import com.lms.readify.category.repository.CategoryRepository;
import com.lms.readify.shared.exception.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class CategoryMapper {

    @Autowired
    private CategoryRepository categoryRepository;


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
    void getParentCategory(CategoryRequestDTO dto, @MappingTarget Category entity) throws EntityNotFoundException {
        if (dto.getParentCategoryId() == null) {
            entity.setParentCategory(null);
        } else {

            Category parent = categoryRepository.findById(dto.getParentCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
            entity.setParentCategory(parent);
        }
    }

}