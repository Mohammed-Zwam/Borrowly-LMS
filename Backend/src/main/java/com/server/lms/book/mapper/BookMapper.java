package com.server.lms.book.mapper;

import com.server.lms.book.dto.request.BookRequest;
import com.server.lms.book.dto.response.BookResponse;
import com.server.lms.book.entity.Book;
import com.server.lms.book.repository.BookRepository;
import com.server.lms.category.entity.Category;
import com.server.lms.category.mapper.CategoryMapper;
import com.server.lms.category.service.CategoryService;
import com.server.lms._shared.exception.DuplicateFieldException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = CategoryMapper.class
)
public abstract class BookMapper {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookRepository bookRepository;

    public abstract BookResponse toBookResponseDTO(Book book);

    public abstract Book toEntity(BookRequest dto);

    public abstract Book toEntity(BookResponse dto);

    public abstract Book toEntity(@MappingTarget Book book, BookRequest dto);

    public abstract List<Book> toEntities(List<BookRequest> dtoList);

    public abstract List<BookResponse> toBookResponseDTOs(List<Book> books);

    @AfterMapping
    public void validateCategory(BookRequest dto, @MappingTarget Book book) {
        if (dto.getCategoryId() == null) {
            book.setCategory(null);
        } else {
            Category category = categoryService.findEntityById(dto.getCategoryId(), "Category");
            book.setCategory(category);
        }

        // VALIDATIONS
        if (bookRepository.existsByIsbn(book.getIsbn()))
            throw new DuplicateFieldException("Book with ISBN: " + book.getIsbn() + " already exists");

        book.isAvailableCopiesValid();
    }
}
