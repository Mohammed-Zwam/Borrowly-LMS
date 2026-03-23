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

    public CategoryTreeResponseDTO update(String id, CategoryRequestDTO dto) {
        Category existingCategory = this.findEntityById(id);
        categoryMapper.toEntity(existingCategory, dto);
        return categoryMapper.toCategoryTreeDTO(categoryRepository.save(existingCategory));
    }

    public void delete(String id) {
        this.findEntityById(id); // check if not exist
        categoryRepository.deleteById(id);
    }

    public CategoryTreeResponseDTO findById(String id) {
        Category category = this.findEntityById(id);
        return categoryMapper.toCategoryTreeDTO(category);
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryMapper.toCategoryDTOs(categoryRepository.findAll());
    }


    public List<CategoryResponseDTO> findAllRoots() {
        return categoryMapper.toCategoryDTOs(findRootEntities());
    }

    public List<CategoryTreeResponseDTO> getTree() {
        return categoryMapper.toCategoryTreeDTOs(findRootEntities());
    }


    public List<CategoryResponseDTO> findSubCategories(String parentId) {
        return categoryMapper.toCategoryDTOs(
                categoryRepository.findByParentCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(parentId)
        );
    }

    public List<CategoryResponseDTO> findAllActiveCategories() {
        return categoryMapper.toCategoryDTOs(
                categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
        );
    }

    public CategoryTreeResponseDTO updateCategoryStatus(String id, boolean isActive) {
        Category category = this.findEntityById(id);
        category.setIsActive(isActive);
        return categoryMapper.toCategoryTreeDTO(categoryRepository.save(category));
    }

    public Long countActiveCategories() {
        return categoryRepository.countByIsActiveTrue();
    }


    //=========== HELPERS ===========//

    private Category findEntityById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category not found with id: " + id
                ));
    }

    private List<Category> findRootEntities() {
        return categoryRepository
                .findByParentCategoryIsNullAndIsActiveTrueOrderByDisplayOrderAsc();
    }
}