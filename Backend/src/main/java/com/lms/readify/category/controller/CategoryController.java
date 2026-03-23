package com.lms.readify.category.controller;

import com.lms.readify.category.dto.request.CategoryRequestDTO;
import com.lms.readify.category.dto.response.CategoryResponseDTO;
import com.lms.readify.category.dto.response.CategoryTreeResponseDTO;
import com.lms.readify.category.service.CategoryService;
import com.lms.readify.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                        ApiResponse.<List<CategoryResponseDTO>>builder()
                                .data(categoryService.findAll())
                                .message("Categories Retrieved Successfully")
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
                        ApiResponse.<CategoryTreeResponseDTO>builder()
                                .data(categoryService.findById(id))
                                .message("Category Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }


    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @RequestBody @Valid CategoryRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<CategoryTreeResponseDTO>builder()
                                .data(categoryService.create(dto))
                                .message("Category created successfully")
                                .success(true)
                                .build()
                );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable @NotBlank String id,
            @RequestBody @Valid CategoryRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<CategoryTreeResponseDTO>builder()
                                .data(categoryService.update(id, dto))
                                .message("Category created successfully")
                                .success(true)
                                .build()
                );
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable @NotBlank String id
    ) {
        categoryService.delete(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<String>builder()
                                .data("Category with id: " + id + " deleted successfully")
                                .message("Category deleted successfully")
                                .success(true)
                                .build()
                );
    }

}
