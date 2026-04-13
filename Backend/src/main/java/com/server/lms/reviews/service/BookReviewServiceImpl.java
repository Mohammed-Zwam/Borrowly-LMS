package com.server.lms.reviews.service;

import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms._shared.exception.DuplicateFieldException;
import com.server.lms._shared.exception.EntityNotFoundException;
import com.server.lms.book.entity.Book;
import com.server.lms.book.service.BookService;
import com.server.lms.loans.enums.BookLoanState;
import com.server.lms.loans.service.BookLoanService;
import com.server.lms.reviews.dto.request.CreateBookReviewRequest;
import com.server.lms.reviews.dto.request.UpdateBookReviewRequest;
import com.server.lms.reviews.dto.response.BookReviewResponse;
import com.server.lms.reviews.entity.BookReview;
import com.server.lms.reviews.exception.BookReviewException;
import com.server.lms.reviews.mapper.BookReviewMapper;
import com.server.lms.reviews.repository.BookReviewRepository;
import com.server.lms.user.entity.User;
import com.server.lms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final UserService userService;
    private final BookService bookService;
    private final BookReviewMapper bookReviewMapper;
    private final BookLoanService bookLoanService;

    @Override
    public BookReviewResponse create(CreateBookReviewRequest createBookReviewRequest) {
        User user = userService.getCurrentUser();
        Book book = bookService.findEntityById(createBookReviewRequest.getBookId());

        if (bookReviewRepository.existsByBookIdAndUserId(book.getId(), user.getId())) {
            throw new DuplicateFieldException("Book review already exists");
        }

        if (!hasUserReadBook(book.getId(), user.getId())) {
            throw new BookReviewException("You have not read this book");
        }

        BookReview bookReview = BookReview.builder()
                .book(book)
                .user(user)
                .title(createBookReviewRequest.getTitle())
                .rating(createBookReviewRequest.getRating())
                .reviewDescription(createBookReviewRequest.getReviewDescription())
                .build();


        return bookReviewMapper.toDTO(
                bookReviewRepository.save(bookReview)
        );
    }

    @Override
    public BookReviewResponse update(String reviewId, UpdateBookReviewRequest updateBookReviewRequest) {
        User user = userService.getCurrentUser();
        BookReview bookReview = findEntityById(reviewId);

        if (!Objects.equals(user.getId(), bookReview.getUser().getId())) {
            throw new BookReviewException("Not authorized to update this book");
        }

        bookReview = bookReviewMapper.update(updateBookReviewRequest, bookReview);

        return bookReviewMapper.toDTO(
                bookReviewRepository.save(bookReview)
        );
    }

    @Override
    public void delete(String reviewId) {
        User user = userService.getCurrentUser();
        BookReview bookReview = findEntityById(reviewId);
        if (!Objects.equals(user.getId(), bookReview.getUser().getId())) {
            throw new BookReviewException("Not authorized to delete this book");
        }
        bookReviewRepository.delete(bookReview);
    }

    @Override
    public BookReviewResponse getUserReviewForBook(String bookId) {
        User user = userService.getCurrentUser();
        BookReview bookReview = bookReviewRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book review for book with id " + bookId + " not found"));
        return bookReviewMapper.toDTO(bookReview);
    }

    @Override
    public PageResponse<BookReviewResponse> getAllReviewsForBook(String bookId, PageRequestDTO pageRequestDTO) {
        if (!bookReviewRepository.existsByBookId(bookId)) {
            throw new EntityNotFoundException("No reviews found for book with id " + bookId);
        }

        Page<BookReview> bookReviews = bookReviewRepository.findByBookId(bookId, pageRequestDTO.generatePageable());
        return PageResponse.<BookReviewResponse>builder()
                .content(bookReviews.getContent()
                        .stream()
                        .map(bookReviewMapper::toDTO)
                        .collect(Collectors.toList())
                ).build()
                .setPageInfo(bookReviews);
    }

    @Override
    public PageResponse<BookReviewResponse> getAllReviews(PageRequestDTO pageRequestDTO) {
        Page<BookReview> bookReviews = bookReviewRepository.findAll(pageRequestDTO.generatePageable());
        return PageResponse.<BookReviewResponse>builder()
                .content(bookReviews.getContent()
                        .stream()
                        .map(bookReviewMapper::toDTO)
                        .collect(Collectors.toList())
                ).build()
                .setPageInfo(bookReviews);
    }



    // ======== HELPERS ======== //
    @Override
    public BookReview findEntityById(String id) {
        return bookReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book review with id " + id + " not found"));
    }

    private boolean hasUserReadBook(String bookId, String userId) {
        return bookLoanService.existsByUserIdAndBookIdAndStatus(userId, bookId, BookLoanState.RETURNED);
    }

}
