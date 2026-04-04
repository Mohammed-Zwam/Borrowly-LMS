package com.server.lms.book.dto.request;

import com.server.lms._shared.dto.PageRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchRequestDTO extends PageRequestDTO {
    private String searchTerm;
    private String categoryId;
    private Boolean availableOnly;

}
