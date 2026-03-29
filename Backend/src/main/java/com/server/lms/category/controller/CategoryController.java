package com.server.lms.category.controller;

import com.server.lms.category.dto.request.CategoryRequest;
import com.server.lms.category.dto.response.CategoryResponse;
import com.server.lms.category.dto.response.CategoryTreeResponse;
import com.server.lms.category.service.CategoryService;
import com.server.lms._shared.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<CategoryResponse>>builder()
                                .data(categoryService.findAll())
                                .message("Categories Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }

    @GetMapping("/active/roots")
    public ResponseEntity<ApiResponse<?>> findRootsWithoutChildren() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<CategoryResponse>>builder()
                                .data(categoryService.findAllRoots())
                                .message("Root Categories Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }

    @GetMapping("/active/tree")
    public ResponseEntity<ApiResponse<?>> findRootsWithChildren() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<CategoryTreeResponse>>builder()
                                .data(categoryService.getTree())
                                .message("Categories Tree Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }


    @GetMapping("/{parentId}/sub-categories")
    public ResponseEntity<ApiResponse<?>> findSubCategories(
            @PathVariable @NotBlank String parentId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<CategoryResponse>>builder()
                                .data(categoryService.findSubCategories(parentId))
                                .message("Sub-Categories Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<?>> findAllActiveCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<CategoryResponse>>builder()
                                .data(categoryService.findAllActiveCategories())
                                .message("Active Categories Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> findById(
            @PathVariable @NotBlank String id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<CategoryTreeResponse>builder()
                                .data(categoryService.findById(id))
                                .message("Category Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> create(
            @RequestBody @Valid CategoryRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<CategoryTreeResponse>builder()
                                .data(categoryService.create(dto))
                                .message("Category created successfully")
                                .success(true)
                                .build()
                );
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable @NotBlank String id,
            @RequestBody @Valid CategoryRequest dto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<CategoryTreeResponse>builder()
                                .data(categoryService.update(id, dto))
                                .message("Category updated successfully")
                                .success(true)
                                .build()
                );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable @NotBlank String id
    ) {
        categoryService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<String>builder()
                                .data("Category with id: " + id + " deleted successfully")
                                .message("Category deleted successfully")
                                .success(true)
                                .build()
                );
    }


    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deactivate(
            @PathVariable @NotBlank String id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<CategoryTreeResponse>builder()
                                .data(categoryService.updateCategoryStatus(id, false))
                                .message("Category Deactivated Successfully")
                                .success(true)
                                .build()
                );
    }


    @GetMapping("/active/count")
    public ResponseEntity<ApiResponse<?>> countActive() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<Long>builder()
                                .data(categoryService.countActiveCategories())
                                .message("Active Categories Count Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }


}