package com.server.lms.reviews.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookReviewResponse {
    private String id;
    private String userName;
    private String userId;
    private String bookId;
    private String bookTitle;
    private Integer rating;
    private String reviewDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
}
