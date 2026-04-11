package com.server.lms.bookmark.dto.response;

import com.server.lms.book.dto.response.BookResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookmarkResponse {
    private String id;
    private String userId;
    private String userName;
    private BookResponse book;
    private LocalDateTime addedAt;
}
