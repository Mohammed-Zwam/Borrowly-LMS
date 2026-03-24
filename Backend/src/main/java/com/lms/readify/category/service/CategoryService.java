package com.lms.readify.category.service;

import com.lms.readify.category.dto.request.CategoryRequestDTO;
import com.lms.readify.category.dto.response.CategoryResponseDTO;
import com.lms.readify.category.dto.response.CategoryTreeResponseDTO;
import com.lms.readify.category.entity.Category;

import java.util.List;

public interface CategoryService {
    // CRUD
    CategoryTreeResponseDTO create(CategoryRequestDTO dto);

    CategoryTreeResponseDTO update(String id, CategoryRequestDTO dto);

    void delete(String id);

    CategoryTreeResponseDTO findById(String id);

    List<CategoryResponseDTO> findAll();

    List<CategoryResponseDTO> findAllRoots();

    List<CategoryTreeResponseDTO> getTree();

    List<CategoryResponseDTO> findSubCategories(String parentId);

    // Status management
    CategoryTreeResponseDTO updateCategoryStatus(String id, boolean isActive);

    List<CategoryResponseDTO> findAllActiveCategories();

    // Statistics
    Long countActiveCategories();


    // HELPERS
     Category findEntityById(String id, String entityName);
}
