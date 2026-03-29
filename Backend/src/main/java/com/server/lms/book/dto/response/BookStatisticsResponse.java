package com.server.lms.book.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookStatisticsResponse {
    private Long totalActiveBooks;
    private Long totalAvailableBooks;
}
