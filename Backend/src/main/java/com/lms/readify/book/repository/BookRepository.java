package com.lms.readify.book.repository;

import com.lms.readify.book.entity.Book;
import com.lms.readify.shared.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends BaseRepository<Book, String>, JpaSpecificationExecutor<Book> {

    Optional<Book> findByIsbn(String isbn);

    Boolean existsByIsbn(String isbn);
    
    Long countByIsActiveTrue();

    @Query("SELECT COUNT(b) FROM Book b WHERE b.isActive = true AND b.availableCopies > 0")
    Long countAvailableBooks();
}
