package com.lms.readify.book.dto.response;

import com.lms.readify.category.dto.response.CategoryResponseDTO;
import com.lms.readify.category.entity.Category;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookResponseDTO {

    private String id;

    private String isbn;

    private String title;

    private String author;

    private CategoryResponseDTO category;

    private String publisher;

    private LocalDate publicationDate;

    private String language;

    private Integer pages;

    private String description;

    private Integer totalCopies;

    private Integer availableCopies;

    private BigDecimal price;

    private String coverImageUrl;

    private Boolean isActive = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
