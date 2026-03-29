package com.server.lms.book.repository;

import com.server.lms.book.entity.Book;
import com.server.lms._shared.base.BaseRepository;
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
