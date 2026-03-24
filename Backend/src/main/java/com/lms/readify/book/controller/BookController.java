package com.lms.readify.book.controller;

import com.lms.readify.book.dto.request.BookRequestDTO;
import com.lms.readify.book.dto.request.BookSearchRequestDTO;
import com.lms.readify.book.dto.response.BookResponseDTO;
import com.lms.readify.book.dto.response.BookStatisticsResponseDTO;
import com.lms.readify.book.service.BookService;
import com.lms.readify.shared.dto.ApiResponse;
import com.lms.readify.shared.dto.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                ApiResponse.<List<BookResponseDTO>>builder()
                        .success(true)
                        .data(bookService.findAll())
                        .message("Books fetched successfully")
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createBook(
            @RequestBody @Valid BookRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BookResponseDTO>builder()
                        .success(true)
                        .data(bookService.create(dto))
                        .message("Book created successfully")
                        .build()
        );
    }


    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<?>> createBooks(
            @RequestBody @Valid List<BookRequestDTO> dtoList
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<List<BookResponseDTO>>builder()
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
                ApiResponse.<BookResponseDTO>builder()
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
                ApiResponse.<BookResponseDTO>builder()
                        .success(true)
                        .data(bookService.findByIsbn(isbn))
                        .message("Book fetched successfully")
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable @NotBlank String id,
            @RequestBody @Valid BookRequestDTO dto

    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<BookResponseDTO>builder()
                        .success(true)
                        .data(bookService.update(id, dto))
                        .message("Book updated successfully")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
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
    public ResponseEntity<ApiResponse<?>> deactivate(
            @PathVariable @NotBlank String id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<BookResponseDTO>builder()
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
                ApiResponse.<PageResponse<BookResponseDTO>>builder()
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
                        ApiResponse.<BookStatisticsResponseDTO>builder()
                                .data(bookService.getBookStatistics())
                                .message("Active Books Count Retrieved Successfully")
                                .success(true)
                                .build()
                );
    }


}