package com.server.lms.reviews.service;

import com.server.lms._shared.base.BaseService;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.reviews.dto.request.CreateBookReviewRequest;
import com.server.lms.reviews.dto.request.UpdateBookReviewRequest;
import com.server.lms.reviews.dto.response.BookReviewResponse;
import com.server.lms.reviews.entity.BookReview;

public interface BookReviewService extends BaseService<BookReview, String> {
    BookReviewResponse create(CreateBookReviewRequest createBookReviewRequest);

    BookReviewResponse update(String reviewId, UpdateBookReviewRequest updateBookReviewRequest);

    void delete(String reviewId);

    BookReviewResponse getUserReviewForBook(String bookId);

    PageResponse<BookReviewResponse> getAllReviewsForBook(String bookId, PageRequestDTO pageRequestDTO);

    PageResponse<BookReviewResponse> getAllReviews(PageRequestDTO pageRequestDTO);
}
