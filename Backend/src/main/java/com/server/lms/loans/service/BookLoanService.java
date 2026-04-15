package com.server.lms.loans.service;

import com.server.lms._shared.base.BaseService;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.loans.dto.request.BookLoanRequest;
import com.server.lms.loans.dto.request.BookLoanSearchRequest;
import com.server.lms.loans.dto.request.RenewalRequest;
import com.server.lms.loans.dto.request.ReturnBookRequest;
import com.server.lms.loans.dto.response.BookLoanResponse;
import com.server.lms.loans.entity.BookLoan;
import com.server.lms.loans.enums.BookLoanState;

import java.util.List;

public interface BookLoanService extends BaseService<BookLoan, String> {
    BookLoanResponse borrowBook(BookLoanRequest bookLoanRequest);

    BookLoanResponse borrowBook(String userId, BookLoanRequest bookLoanRequest);

    BookLoanResponse returnBook(ReturnBookRequest returnBookRequest);

    BookLoanResponse renewBorrow(RenewalRequest renewalRequest);

    PageResponse<BookLoanResponse> getUserBookLoans(PageRequestDTO pageRequest, BookLoanState status);

    PageResponse<BookLoanResponse> getAllBookLoans(BookLoanSearchRequest bookLoanSearchRequest); // FOR ADMIN

    Integer updateOverdueBookLoan(); // to update status of missed book loans TODO: SCHEDULING JOB

    boolean existsByUserIdAndBookIdAndStatus(String userId, String bookId, BookLoanState state);
}
