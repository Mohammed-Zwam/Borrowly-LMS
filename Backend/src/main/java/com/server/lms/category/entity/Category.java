package com.server.lms.category.entity;


import com.server.lms.book.entity.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;

    private String code;

    private String description;

    private Integer displayOrder;

    @NotNull
    @Column(name = "active")
    private Boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;


    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "category")
    private List<Book> books;


    /*================================[Composite Pattern]================================*/
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory") // BI-Directional Relationship (WITHOUT CREATING NEW TABLE)
    private List<Category> subCategories;
}
