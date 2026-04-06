package com.server.lms.book.service;

import com.server.lms.book.dto.request.BookRequest;
import com.server.lms.book.dto.request.BookSearchRequestDTO;
import com.server.lms.book.dto.response.BookResponse;
import com.server.lms.book.dto.response.BookStatisticsResponse;
import com.server.lms.book.entity.Book;
import com.server.lms.book.mapper.BookMapper;
import com.server.lms.book.repository.BookRepository;
import com.server.lms.book.specification.BookSpecification;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms._shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public BookResponse create(BookRequest dto) {
        Book book = bookMapper.toEntity(dto);
        return bookMapper.toBookResponseDTO(bookRepository.save(book));
    }

    public List<BookResponse> createAll(List<BookRequest> dtoList) {
        List<Book> books = bookMapper.toEntities(dtoList);
        return bookMapper.toBookResponseDTOs(bookRepository.saveAll(books));
    }

    public BookResponse update(String id, BookRequest dto) {
        Book existingBook = findEntityById(id);
        bookMapper.toEntity(existingBook, dto);
        return bookMapper.toBookResponseDTO(bookRepository.save(existingBook));
    }

    public void delete(String id) {
        this.findEntityById(id);
        bookRepository.deleteById(id);
    }

    public BookResponse findById(String id) {
        return bookMapper.toBookResponseDTO(findEntityById(id));
    }

    public List<BookResponse> findAll() {
        return bookMapper.toBookResponseDTOs(bookRepository.findAll());
    }


    public BookResponse changeBookStatus(String id, boolean isActive) {
        Book book = this.findEntityById(id);
        book.setIsActive(isActive);
        return bookMapper.toBookResponseDTO(bookRepository.save(book));
    }

    public PageResponse<BookResponse> searchBooksByFilters(
            BookSearchRequestDTO searchRequest
    ) {
        Specification<Book> spec = Specification
                .where(BookSpecification.hasTitle(searchRequest.getSearchTerm()))
                .and(BookSpecification.hasCategory(searchRequest.getCategoryId()))
                .and(BookSpecification.isAvailable(searchRequest.getAvailableOnly()));


        Pageable pageable = getPageable(searchRequest);

        Page<Book> result = bookRepository.findAll(spec, pageable);

        return PageResponse.<BookResponse>builder()
                .content(bookMapper.toBookResponseDTOs(result.getContent()))
                .build()
                .setPageInfo(result);
    }

    @Override
    public BookStatisticsResponse getBookStatistics() {
        return BookStatisticsResponse.builder()
                .totalActiveBooks(bookRepository.countByIsActiveTrue())
                .totalAvailableBooks(bookRepository.countAvailableBooks())
                .build();
    }


    @Override
    public BookResponse findByIsbn(String isbn) {
        return bookMapper.toBookResponseDTO(
                bookRepository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFoundException("Book not found with ISBN: " + isbn))
        );
    }


    //=========== HELPERS ===========//

    public Book findEntityById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + id
                ));
    }

    private Pageable getPageable(BookSearchRequestDTO searchRequest) {
        int size = searchRequest.getSize();
        size = Math.min(size, 20);
        size = Math.max(size, 1);
        Sort sort = Sort.by(
                searchRequest.getSortOrder().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC
                , searchRequest.getSortBy()
        );
        return PageRequest.of(searchRequest.getPageNumber(), size, sort);
    }
}
