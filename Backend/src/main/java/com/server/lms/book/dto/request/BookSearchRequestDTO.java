package com.server.lms.book.dto.request;

import com.server.lms._shared.dto.PageRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchRequestDTO extends PageRequestDTO {
    private String searchTerm;
    private String categoryId;
    private Boolean availableOnly;

}
