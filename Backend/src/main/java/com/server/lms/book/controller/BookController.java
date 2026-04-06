package com.server.lms.book.controller;

import com.server.lms.book.dto.request.BookRequest;
import com.server.lms.book.dto.request.BookSearchRequestDTO;
import com.server.lms.book.dto.response.BookResponse;
import com.server.lms.book.dto.response.BookStatisticsResponse;
import com.server.lms.book.service.BookService;
import com.server.lms._shared.dto.ApiResponse;
import com.server.lms._shared.dto.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> findAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<BookResponse>>builder()
                        .success(true)
                        .data(bookService.findAll())
                        .message("Books fetched successfully")
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createBook(
            @RequestBody @Valid BookRequest dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .data(bookService.create(dto))
                        .message("Book created successfully")
                        .build()
        );
    }


    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createBooks(
            @RequestBody @Valid List<BookRequest> dtoList
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<List<BookResponse>>builder()
                        .success(true)
                        .data(bookService.createAll(dtoList))
                        .message("Book created successfully")
                        .build()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> findBookById(
            @PathVariable @NotBlank String id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .data(bookService.findById(id))
                        .message("Book fetched successfully")
                        .build()
        );
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<ApiResponse<?>> findBookByIsbn(
            @PathVariable @NotBlank String isbn
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .data(bookService.findByIsbn(isbn))
                        .message("Book fetched successfully")
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable @NotBlank String id,
            @RequestBody @Valid BookRequest dto

    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .data(bookService.update(id, dto))
                        .message("Book updated successfully")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable @NotBlank String id
    ) {
        bookService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<String>builder()
                        .success(true)
                        .data("Book with id: " + id + " deleted successfully")
                        .message("Book deleted successfully")
                        .build()
        );
    }


    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deactivate(
            @PathVariable @NotBlank String id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .data(bookService.changeBookStatus(id, false))
                        .message("Book deactivated successfully")
                        .build()
        );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchBooks(
            @RequestBody @Valid BookSearchRequestDTO searchRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<PageResponse<BookResponse>>builder()
                        .success(true)
                        .data(bookService.searchBooksByFilters(searchRequest))
                        .message("Books fetched successfully")
                        .build()
        );
    }


    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<?>> getBookStatistics() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<BookStatisticsResponse>builder()
                                .data(bookService.getBookStatistics())
                                .message("Active Books Count Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }


}