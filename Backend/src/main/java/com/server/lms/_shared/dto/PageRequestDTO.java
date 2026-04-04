package com.server.lms._shared.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class PageRequestDTO {
    private Integer pageNumber = 0;
    private Integer size = 20;
    private String sortBy = "createdAt";
    private String sortOrder = "desc";

    public Pageable generatePageable() {
        Sort sort = Sort.by(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return PageRequest.of(pageNumber, size, sort);
    }
}