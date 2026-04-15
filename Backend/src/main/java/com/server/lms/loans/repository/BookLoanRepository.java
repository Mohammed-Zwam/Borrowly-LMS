package com.server.lms.loans.repository;

import com.server.lms._shared.base.BaseRepository;
import com.server.lms.loans.entity.BookLoan;
import com.server.lms.loans.enums.BookLoanState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLoanRepository extends BaseRepository<BookLoan, String>, JpaSpecificationExecutor<BookLoan> {

    Page<BookLoan> findByUserId(String userId, Pageable pageable);

    Page<BookLoan> findByUserIdAndBookLoanState(String userId, BookLoanState bookLoanState, Pageable pageable);

    Page<BookLoan> findByBookLoanState(BookLoanState bookLoanState, Pageable pageable);

    @Query("""
                    SELECT 
                    CASE WHEN COUNT(bookLoan) > 0 THEN true ELSE false END
                    FROM BookLoan bookLoan
                    WHERE bookLoan.user.id = :userId AND bookLoan.book.id = :bookId
                    AND (bookLoan.bookLoanState = 'BORROWED' OR bookLoan.bookLoanState = 'RENEWED')
            """)
    boolean hasBookAlreadyBorrowed(String userId, String bookId);


    @Query("""
                    SELECT COUNT(bookLoan) FROM BookLoan bookLoan
                    WHERE bookLoan.user.id = :userId
                    AND (bookLoan.bookLoanState = 'BORROWED' OR bookLoan.bookLoanState = 'OVERDUE' OR bookLoan.bookLoanState = 'RENEWED')
            """)
    int countActiveBookLoansByUser(String userId); // Accurate & Better than field in user / subscription


    @Query("""
                    SELECT COUNT(bookLoan) FROM BookLoan bookLoan
                    WHERE bookLoan.user.id = :userId
                    AND bookLoan.bookLoanState = 'OVERDUE'
            """)
    int countOverdueBookLoansByUser(String userId);


    boolean existsByUserIdAndBookIdAndBookLoanState(String userId, String bookId, BookLoanState bookLoanState);

}
