package com.server.lms.book.entity;


import com.server.lms.category.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @NotNull
    private String title;

    private String author;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @AssertTrue(message = "Available copies must be less than or equal to total copies")
    public boolean isAvailableCopiesValid() {
        if (totalCopies == null || availableCopies == null) return true;
        return availableCopies <= totalCopies;
    }
}
