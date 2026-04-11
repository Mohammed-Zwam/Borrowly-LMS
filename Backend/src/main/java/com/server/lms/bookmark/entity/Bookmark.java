package com.server.lms.bookmark.entity;


import com.server.lms.book.entity.Book;
import com.server.lms.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;


    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    Book book;

    @CreationTimestamp
    private LocalDateTime addedAt;

    @Column(length = 500)
    private String notes;
}
