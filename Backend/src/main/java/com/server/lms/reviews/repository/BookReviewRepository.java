package com.server.lms.reviews.repository;

import com.server.lms._shared.base.BaseRepository;
import com.server.lms.reviews.entity.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReviewRepository extends BaseRepository<BookReview, String> {

    Optional<BookReview> findByBookIdAndUserId(String bookId, String userId);

    Page<BookReview> findByBookId(String bookId, Pageable pageable);

    boolean existsByBookIdAndUserId(String bookId, String userId);

    boolean existsByBookId(String bookId);

}
