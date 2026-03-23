package com.lms.readify.category.repository;

import com.lms.readify.category.entity.Category;
import com.lms.readify.shared.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {

    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();

    List<Category> findByParentCategoryIsNullAndIsActiveTrueOrderByDisplayOrderAsc();

    List<Category> findByParentCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(String parentCategoryId);

    long countByIsActiveTrue();

}
