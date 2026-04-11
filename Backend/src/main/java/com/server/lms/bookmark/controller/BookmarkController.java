package com.server.lms.bookmark.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.book.dto.response.BookResponse;
import com.server.lms.bookmark.dto.response.BookmarkResponse;
import com.server.lms.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> findMyBookmarks(
            @ParameterObject @ModelAttribute PageRequestDTO pageRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<PageResponse<BookmarkResponse>>builder()
                        .success(true)
                        .data(bookmarkService.getMyBookmarks(pageRequest))
                        .message("Bookmarks fetched successfully")
                        .build()
        );
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<ApiResponse<?>> addToBookmarks(
            @PathVariable String bookId,
            @RequestParam(required = false) String notes
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BookmarkResponse>builder()
                        .success(true)
                        .data(bookmarkService.addToBookmarks(bookId, notes))
                        .message("Bookmark added successfully")
                        .build()
        );
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse<?>> removeBookmark(
            @PathVariable String bookId
    ) {
        bookmarkService.removeBookmark(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<Void>builder()
                        .success(true)
                        .data(null)
                        .message("Bookmark deleted successfully")
                        .build()
        );
    }


}
