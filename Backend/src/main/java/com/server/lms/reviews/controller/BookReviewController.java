package com.server.lms.reviews.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.reviews.dto.request.CreateBookReviewRequest;
import com.server.lms.reviews.dto.request.UpdateBookReviewRequest;
import com.server.lms.reviews.dto.response.BookReviewResponse;
import com.server.lms.reviews.service.BookReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class BookReviewController {

    private final BookReviewService bookReviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createReview(
            @RequestBody @Valid CreateBookReviewRequest createBookReviewRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<BookReviewResponse>builder()
                                .success(true)
                                .message("Book review created successfully")
                                .data(bookReviewService.create(createBookReviewRequest))
                                .build()
                );
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<?>> updateReview(
            @PathVariable @Valid String reviewId,
            @RequestBody @Valid UpdateBookReviewRequest updateBookReviewRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<BookReviewResponse>builder()
                                .success(true)
                                .message("Book review updated successfully")
                                .data(bookReviewService.update(reviewId, updateBookReviewRequest))
                                .build()
                );
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<?>> deleteReview(
            @PathVariable @Valid String reviewId
    ) {
        bookReviewService.delete(reviewId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<Void>builder()
                                .success(true)
                                .message("Book review deleted successfully")
                                .data(null)
                                .build()
                );
    }


    @GetMapping("/book/{bookId}/me")
    public ResponseEntity<ApiResponse<?>> getUserReviewForBook(
            @PathVariable @Valid String bookId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<BookReviewResponse>builder()
                                .success(true)
                                .message("Book review retrieved successfully")
                                .data(bookReviewService.getUserReviewForBook(bookId))
                                .build()
                );
    }


    @GetMapping("/book/{bookId}/reviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> getReviewsForBook(
            @PathVariable @Valid String bookId,
            @ParameterObject @ModelAttribute PageRequestDTO pageRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<BookReviewResponse>>builder()
                                .success(true)
                                .message("Book reviews retrieved successfully")
                                .data(bookReviewService.getAllReviewsForBook(bookId, pageRequestDTO))
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllReviews(
            @ParameterObject @ModelAttribute PageRequestDTO pageRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<BookReviewResponse>>builder()
                                .success(true)
                                .message("Book reviews retrieved successfully")
                                .data(bookReviewService.getAllReviews(pageRequestDTO))
                                .build()
                );
    }
}
