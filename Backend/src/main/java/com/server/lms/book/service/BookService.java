package com.server.lms.book.service;

import com.server.lms._shared.base.BaseService;
import com.server.lms.book.dto.request.BookRequest;
import com.server.lms.book.dto.request.BookSearchRequestDTO;
import com.server.lms.book.dto.response.BookResponse;
import com.server.lms.book.dto.response.BookStatisticsResponse;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.book.entity.Book;

import java.util.List;

public interface BookService extends BaseService<Book, String> {
    // CRUD
    BookResponse create(BookRequest dto);

    BookResponse update(String id, BookRequest dto);

    void delete(String id);

    BookResponse findById(String id);

    List<BookResponse> findAll();

    List<BookResponse> createAll(List<BookRequest> dtoList);

    BookResponse changeBookStatus(String id, boolean isActive);

    // Search with filters and pagination
    PageResponse<BookResponse> searchBooksByFilters(BookSearchRequestDTO searchRequest);

    BookStatisticsResponse getBookStatistics();

    BookResponse findByIsbn(String isbn);
}
