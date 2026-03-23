package com.lms.readify.category.service;


import com.lms.readify.category.dto.request.CategoryRequestDTO;
import com.lms.readify.category.dto.response.CategoryResponseDTO;
import com.lms.readify.category.dto.response.CategoryTreeResponseDTO;
import com.lms.readify.category.entity.Category;
import com.lms.readify.category.mapper.CategoryMapper;
import com.lms.readify.category.repository.CategoryRepository;
import com.lms.readify.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryTreeResponseDTO create(CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toCategoryTreeDTO(categoryRepository.save(category));
    }

    public CategoryTreeResponseDTO update(String id, CategoryRequestDTO dto) throws EntityNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        category = categoryMapper.toEntity(category, dto);
        return categoryMapper.toCategoryTreeDTO(categoryRepository.save(category));
    }

    public void delete(String id) {
        categoryRepository.deleteById(id);
    }

    public CategoryTreeResponseDTO findById(String id) throws EntityNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

        return categoryMapper.toCategoryTreeDTO(category);
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryMapper.toCategoryDTOs(categoryRepository.findAll());
    }

    public List<CategoryTreeResponseDTO> findRootCategoriesWithSubCategories() {
        return categoryMapper.toCategoryTreeDTOs(
                categoryRepository.findByParentCategoryIsNullAndIsActiveTrueOrderByDisplayOrderAsc()
        );
    }
}
