package com.server.lms.category.service;

import com.server.lms.category.dto.request.CategoryRequest;
import com.server.lms.category.dto.response.CategoryResponse;
import com.server.lms.category.dto.response.CategoryTreeResponse;
import com.server.lms.category.entity.Category;

import java.util.List;

public interface CategoryService {
    // CRUD
    CategoryTreeResponse create(CategoryRequest dto);

    CategoryTreeResponse update(String id, CategoryRequest dto);

    void delete(String id);

    CategoryTreeResponse findById(String id);

    List<CategoryResponse> findAll();

    List<CategoryResponse> findAllRoots();

    List<CategoryTreeResponse> getTree();

    List<CategoryResponse> findSubCategories(String parentId);

    // Status management
    CategoryTreeResponse updateCategoryStatus(String id, boolean isActive);

    List<CategoryResponse> findAllActiveCategories();

    // Statistics
    Long countActiveCategories();


    // HELPERS
     Category findEntityById(String id, String entityName);
}
