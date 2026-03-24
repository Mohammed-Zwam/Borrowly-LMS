package com.lms.readify.book.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookStatisticsResponseDTO {
    private Long totalActiveBooks;
    private Long totalAvailableBooks;
}
