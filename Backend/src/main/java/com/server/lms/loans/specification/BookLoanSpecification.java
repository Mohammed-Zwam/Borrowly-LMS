package com.server.lms.loans.specification;


import com.server.lms.book.entity.Book;
import com.server.lms.loans.dto.request.BookLoanSearchRequest;
import com.server.lms.loans.entity.BookLoan;
import com.server.lms.loans.enums.BookLoanState;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BookLoanSpecification {
    private static Specification<BookLoan> hasBookId(String bookId) {
        return (root, query, cb) ->
                cb.equal(root.get("book").get("id"), bookId);
    }

    private static Specification<BookLoan> hasUserId(String userId) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);
    }

    private static Specification<BookLoan> hasBookLoanState(BookLoanState bookLoanState) {
        return (root, query, cb) ->
                cb.equal(root.get("bookLoanState"), bookLoanState);
    }


    public static Specification<BookLoan> byOverdueOnly(LocalDate overDueDate) {
        return (root, query, cb) -> {
            Predicate statePredicate = cb.or(
                    cb.equal(root.get("bookLoanState"), BookLoanState.OVERDUE),
                    cb.equal(root.get("bookLoanState"), BookLoanState.BORROWED)
            );

            if (overDueDate != null) {
                Predicate datePredicate = cb.lessThan(root.get("dueDate"), overDueDate);
                return cb.and(statePredicate, datePredicate);
            }

            return statePredicate;
        };
    }

    private static Specification<BookLoan> byUnpaidFinesOnly() {
        return (root, query, cb) ->
                cb.equal(root.get("bookLoanState"), BookLoanState.OVERDUE); // TODO
    }

    private static Specification<BookLoan> byDateRange(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) ->
                cb.and(
                        cb.greaterThanOrEqualTo(root.get("borrowingDate"), startDate),
                        cb.lessThanOrEqualTo(root.get("borrowingDate"), endDate)
                );
    }


    public static Specification<BookLoan> buildSpecificationByBookLoanSearchRequest(BookLoanSearchRequest bookLoanSearchRequest) {
        Specification<BookLoan> specification = Specification.unrestricted();

        if (bookLoanSearchRequest.isOverdueOnly()) {
            specification = specification.and(byOverdueOnly(LocalDate.now()));
        }
        if (bookLoanSearchRequest.isUnpaidFinesOnly()) {
            specification = Specification.where(byUnpaidFinesOnly());
        }
        if (bookLoanSearchRequest.getUserId() != null) {
            specification = specification.and(hasUserId(bookLoanSearchRequest.getUserId()));
        }
        if (bookLoanSearchRequest.getBookId() != null) {
            specification = specification.and(hasBookId(bookLoanSearchRequest.getBookId()));
        }
        if (bookLoanSearchRequest.getBookLoanState() != null) {
            specification = specification.and(hasBookLoanState(bookLoanSearchRequest.getBookLoanState()));
        }
        if (bookLoanSearchRequest.getStartDate() != null && bookLoanSearchRequest.getEndDate() != null) {
            specification = specification.and(byDateRange(bookLoanSearchRequest.getStartDate(), bookLoanSearchRequest.getEndDate()));
        }

        return specification;

    }
}
