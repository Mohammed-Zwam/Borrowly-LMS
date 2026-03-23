package com.lms.readify.category.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Category name is required")
    private String name;

    @NotBlank(message = "Category code is required")
    private String code;

    @NotBlank(message = "Category description is required")
    @Size(max = 500, message = "Category description must not exceed 500 characters")
    private String description;

    @Min(value = 0, message = "Display Order cannot be less than zero")
    private Integer displayOrder;

    @NotNull
    @Column(name = "active")
    private Boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    /*
        @OneToMany(cascade = CascadeType.PERSIST)
        private List<Book> books;
    */



    /*================================[Composite Pattern]================================*/
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory") // BI-Directional Relationship (WITHOUT CREATING NEW TABLE)
    private List<Category> subCategories;
}
