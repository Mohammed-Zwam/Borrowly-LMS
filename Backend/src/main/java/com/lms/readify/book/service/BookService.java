package com.lms.readify.book.service;

import com.lms.readify.book.dto.request.BookRequestDTO;
import com.lms.readify.book.dto.request.BookSearchRequestDTO;
import com.lms.readify.book.dto.response.BookResponseDTO;
import com.lms.readify.book.dto.response.BookStatisticsResponseDTO;
import com.lms.readify.shared.dto.PageResponse;

import java.util.List;

public interface BookService {
    // CRUD
    BookResponseDTO create(BookRequestDTO dto);

    BookResponseDTO update(String id, BookRequestDTO dto);

    void delete(String id);

    BookResponseDTO findById(String id);

    List<BookResponseDTO> findAll();

    List<BookResponseDTO> createAll(List<BookRequestDTO> dtoList);


    BookResponseDTO changeBookStatus(String id, boolean isActive);

    // Search with filters and pagination
    PageResponse<BookResponseDTO> searchBooksByFilters(BookSearchRequestDTO searchRequest);

    BookStatisticsResponseDTO getBookStatistics();

    BookResponseDTO findByIsbn(String isbn);
}
