package com.server.lms.category.service;


import com.server.lms.category.dto.request.CategoryRequest;
import com.server.lms.category.dto.response.CategoryResponse;
import com.server.lms.category.dto.response.CategoryTreeResponse;
import com.server.lms.category.entity.Category;
import com.server.lms.category.mapper.CategoryMapper;
import com.server.lms.category.repository.CategoryRepository;
import com.server.lms._shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryTreeResponse create(CategoryRequest dto) {
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toCategoryTreeDTO(categoryRepository.save(category));
    }

    public CategoryTreeResponse update(String id, CategoryRequest dto) {
        Category existingCategory = this.findEntityById(id, "Category");
        categoryMapper.toEntity(existingCategory, dto);
        return categoryMapper.toCategoryTreeDTO(categoryRepository.save(existingCategory));
    }

    public void delete(String id) {
        this.findEntityById(id, "Category"); // check if not exist
        categoryRepository.deleteById(id);
    }

    public CategoryTreeResponse findById(String id) {
        Category category = this.findEntityById(id, "Category");
        return categoryMapper.toCategoryTreeDTO(category);
    }

    public List<CategoryResponse> findAll() {
        return categoryMapper.toCategoryDTOs(categoryRepository.findAll());
    }


    public List<CategoryResponse> findAllRoots() {
        return categoryMapper.toCategoryDTOs(findRootEntities());
    }

    public List<CategoryTreeResponse> getTree() {
        return categoryMapper.toCategoryTreeDTOs(findRootEntities());
    }


    public List<CategoryResponse> findSubCategories(String parentId) {
        return categoryMapper.toCategoryDTOs(
                categoryRepository.findByParentCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(parentId)
        );
    }

    public List<CategoryResponse> findAllActiveCategories() {
        return categoryMapper.toCategoryDTOs(
                categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
        );
    }

    public CategoryTreeResponse updateCategoryStatus(String id, boolean isActive) {
        Category category = this.findEntityById(id, "Category");
        category.setIsActive(isActive);
        return categoryMapper.toCategoryTreeDTO(categoryRepository.save(category));
    }

    public Long countActiveCategories() {
        return categoryRepository.countByIsActiveTrue();
    }


    //=========== HELPERS ===========//

    public Category findEntityById(String id, String entityName) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        entityName + " not found with id: " + id
                ));
    }

    private List<Category> findRootEntities() {
        return categoryRepository
                .findByParentCategoryIsNullAndIsActiveTrueOrderByDisplayOrderAsc();
    }
}