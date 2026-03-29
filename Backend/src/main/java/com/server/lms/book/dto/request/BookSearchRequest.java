package com.server.lms.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchRequest {
    private String searchTerm;
    private String categoryId;
    private Boolean availableOnly;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "createdAt";
    private String sortOrder = "desc";
}
