package com.server.lms.book.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookRequest {

    @Size(max = 20)
    @NotBlank(message = "Book isbn is required")
    @Column(unique = true, nullable = false)
    private String isbn;

    @NotBlank(message = "Book title is required")
    @Size(min = 1, max = 255, message = "Book title must be between 1 and 255 characters")
    @NotNull
    private String title;

    @NotBlank(message = "Book author is required")
    @Size(min = 1, max = 255, message = "Book title must be between 1 and 255 characters")
    private String author;

    @NotNull(message = "Category id is required")
    private String categoryId;

    @Size(max = 100)
    private String publisher;

    private LocalDate publicationDate;

    @Size(max = 20, message = "Language must be between 1 and 20 characters")
    private String language;

    @Min(value = 1, message = "Pages must be greater than or equal to 1")
    @Max(value = 50000, message = "Pages must be less than or equal to 50000")
    private Integer pages;

    @Size(max = 2000, message = "Description must be less than or equal to 2000 characters")
    private String description;

    @Min(value = 0, message = "Total copies must be greater than or equal to 0")
    @NotNull(message = "Total copies is required")
    private Integer totalCopies;

    @Min(value = 0, message = "Available Copies must be greater than or equal to 0")
    @NotNull(message = "Available Copies is required")
    private Integer availableCopies;

    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid decimal number")
    private BigDecimal price;

    @Size(max = 500, message = "Cover image URL must be less than or equal to 500 characters")
    private String coverImageUrl;

    private Boolean isActive = true;

}
