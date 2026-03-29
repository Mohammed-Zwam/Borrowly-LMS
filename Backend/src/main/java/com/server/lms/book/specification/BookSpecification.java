package com.server.lms.book.specification;


import com.server.lms.book.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> hasTitle(String searchTerm) {
        return (root, query, cb) ->
                searchTerm == null ? null :
                        cb.or(
                                cb.like(root.get("title"), "%" + searchTerm + "%"),
                                cb.like(root.get("author"), "%" + searchTerm + "%"),
                                cb.like(root.get("isbn"), "%" + searchTerm + "%")
                        );
    }

    public static Specification<Book> hasCategory(String categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null :
                        cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Book> isAvailable(Boolean availableOnly) {
        return (root, query, cb) ->
                Boolean.TRUE.equals(availableOnly) ?
                        cb.greaterThan(root.get("availableCopies"), 0) : null;
    }
}
