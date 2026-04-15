package com.server.lms.loans.controller;

import com.server.lms._shared.dto.ApiResponse;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms.loans.dto.request.BookLoanRequest;
import com.server.lms.loans.dto.request.BookLoanSearchRequest;
import com.server.lms.loans.dto.request.RenewalRequest;
import com.server.lms.loans.dto.request.ReturnBookRequest;
import com.server.lms.loans.dto.response.BookLoanResponse;
import com.server.lms.loans.enums.BookLoanState;
import com.server.lms.loans.service.BookLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book-loans")
@RequiredArgsConstructor
public class BookLoanController {

    private final BookLoanService bookLoanService;


    @PostMapping("/loan")
    public ResponseEntity<ApiResponse<?>> borrowBook(
            @RequestBody @Valid BookLoanRequest bookLoanRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<BookLoanResponse>builder()
                                .success(true)
                                .message("Book borrowed successfully")
                                .data(bookLoanService.borrowBook(bookLoanRequest))
                                .build()
                );
    }

    @PostMapping("/user/{id}/loan")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> borrowBookForUser(
            @PathVariable String id,
            @RequestBody @Valid BookLoanRequest bookLoanRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<BookLoanResponse>builder()
                                .success(true)
                                .message("Book borrowed successfully")
                                .data(bookLoanService.borrowBook(id, bookLoanRequest))
                                .build()
                );
    }

    @PostMapping("/return")
    public ResponseEntity<ApiResponse<?>> returnBook(
            @RequestBody @Valid ReturnBookRequest returnBookRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<BookLoanResponse>builder()
                                .success(true)
                                .message("Book returned successfully")
                                .data(bookLoanService.returnBook(returnBookRequest))
                                .build()
                );
    }

    @PostMapping("/renewal")
    public ResponseEntity<ApiResponse<?>> renewBorrow(
            @RequestBody @Valid RenewalRequest renewalRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<BookLoanResponse>builder()
                                .success(true)
                                .message("Book renew successfully")
                                .data(bookLoanService.renewBorrow(renewalRequest))
                                .build()
                );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getUserBookLoans(
            @ParameterObject @ModelAttribute PageRequestDTO pageRequest,
            @RequestParam(required = false) BookLoanState bookLoanState
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<BookLoanResponse>>builder()
                                .success(true)
                                .message("Book loans retrieved successfully")
                                .data(bookLoanService.getUserBookLoans(pageRequest, bookLoanState))
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getBookLoans(
            @ParameterObject @ModelAttribute BookLoanSearchRequest bookLoanSearchRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<PageResponse<BookLoanResponse>>builder()
                                .success(true)
                                .message("Book retrieved successfully")
                                .data(bookLoanService.getAllBookLoans(bookLoanSearchRequest))
                                .build()
                );
    }

    @PostMapping("update-overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateOverDueBookLoans() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<Integer>builder()
                                .success(true)
                                .message("Overdue book loans updated successfully")
                                .data(bookLoanService.updateOverdueBookLoan())
                                .build()
                );
    }

}
