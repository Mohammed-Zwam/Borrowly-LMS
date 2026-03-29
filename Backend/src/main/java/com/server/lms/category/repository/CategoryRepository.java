package com.server.lms.category.repository;

import com.server.lms.category.entity.Category;
import com.server.lms._shared.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {

    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();

    List<Category> findByParentCategoryIsNullAndIsActiveTrueOrderByDisplayOrderAsc();

    List<Category> findByParentCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(String parentCategoryId);

    long countByIsActiveTrue();

}
