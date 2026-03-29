package com.server.lms.book.dto.response;

import com.server.lms.category.dto.response.CategoryResponse;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookResponse {

    private String id;

    private String isbn;

    private String title;

    private String author;

    private CategoryResponse category;

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
