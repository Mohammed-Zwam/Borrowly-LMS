package com.lms.readify.book.mapper;

import com.lms.readify.book.dto.request.BookRequestDTO;
import com.lms.readify.book.dto.response.BookResponseDTO;
import com.lms.readify.book.entity.Book;
import com.lms.readify.book.repository.BookRepository;
import com.lms.readify.category.entity.Category;
import com.lms.readify.category.mapper.CategoryMapper;
import com.lms.readify.category.service.CategoryService;
import com.lms.readify.shared.exception.DuplicateFieldException;
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

    public abstract BookResponseDTO toBookResponseDTO(Book book);

    public abstract Book toEntity(BookRequestDTO dto);

    public abstract Book toEntity(BookResponseDTO dto);

    public abstract Book toEntity(@MappingTarget Book book, BookRequestDTO dto);

    public abstract List<Book> toEntities(List<BookRequestDTO> dtoList);

    public abstract List<BookResponseDTO> toBookResponseDTOs(List<Book> books);

    @AfterMapping
    public void validateCategory(BookRequestDTO dto, @MappingTarget Book book) {
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
