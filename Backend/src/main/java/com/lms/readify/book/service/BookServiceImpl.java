package com.lms.readify.book.service;

import com.lms.readify.book.dto.request.BookRequestDTO;
import com.lms.readify.book.dto.request.BookSearchRequestDTO;
import com.lms.readify.book.dto.response.BookResponseDTO;
import com.lms.readify.book.dto.response.BookStatisticsResponseDTO;
import com.lms.readify.book.entity.Book;
import com.lms.readify.book.mapper.BookMapper;
import com.lms.readify.book.repository.BookRepository;
import com.lms.readify.book.specification.BookSpecification;
import com.lms.readify.shared.dto.PageResponse;
import com.lms.readify.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public BookResponseDTO create(BookRequestDTO dto) {
        Book book = bookMapper.toEntity(dto);

        return bookMapper.toBookResponseDTO(bookRepository.save(book));
    }

    public List<BookResponseDTO> createAll(List<BookRequestDTO> dtoList) {
        List<Book> books = bookMapper.toEntities(dtoList);
        return bookMapper.toBookResponseDTOs(bookRepository.saveAll(books));
    }

    public BookResponseDTO update(String id, BookRequestDTO dto) {
        Book existingBook = findEntityById(id);
        bookMapper.toEntity(existingBook, dto);
        return bookMapper.toBookResponseDTO(bookRepository.save(existingBook));
    }

    public void delete(String id) {
        this.findEntityById(id);
        bookRepository.deleteById(id);
    }

    public BookResponseDTO findById(String id) {
        return bookMapper.toBookResponseDTO(findEntityById(id));
    }

    public List<BookResponseDTO> findAll() {
        return bookMapper.toBookResponseDTOs(bookRepository.findAll());
    }


    public BookResponseDTO changeBookStatus(String id, boolean isActive) {
        Book book = this.findEntityById(id);
        book.setIsActive(isActive);
        return bookMapper.toBookResponseDTO(bookRepository.save(book));
    }

    public PageResponse<BookResponseDTO> searchBooksByFilters(
            BookSearchRequestDTO searchRequest
    ) {
        Specification<Book> spec = Specification
                .where(BookSpecification.hasTitle(searchRequest.getSearchTerm()))
                .and(BookSpecification.hasCategory(searchRequest.getCategoryId()))
                .and(BookSpecification.isAvailable(searchRequest.getAvailableOnly()));


        Pageable pageable = getPageable(searchRequest);

        Page<Book> result = bookRepository.findAll(spec, pageable);

        return PageResponse.<BookResponseDTO>builder()
                .content(bookMapper.toBookResponseDTOs(result.getContent()))
                .pageNumber(result.getNumber())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .isLastPage(result.isLast())
                .isFirstPage(result.isFirst())
                .isEmpty(result.isEmpty())
                .build();
    }

    @Override
    public BookStatisticsResponseDTO getBookStatistics() {
        return BookStatisticsResponseDTO.builder()
                .totalActiveBooks(bookRepository.countByIsActiveTrue())
                .totalAvailableBooks(bookRepository.countAvailableBooks())
                .build();
    }


    @Override
    public BookResponseDTO findByIsbn(String isbn) {
        return bookMapper.toBookResponseDTO(
                bookRepository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFoundException("Book not found with ISBN: " + isbn))
        );
    }


    //=========== HELPERS ===========//

    private Book findEntityById(String id) {
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
        return PageRequest.of(searchRequest.getPage(), size, sort);
    }
}
