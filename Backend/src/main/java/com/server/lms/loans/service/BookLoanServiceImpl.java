package com.server.lms.loans.service;

import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms._shared.exception.EntityNotFoundException;
import com.server.lms.book.entity.Book;
import com.server.lms.book.repository.BookRepository;
import com.server.lms.book.service.BookService;
import com.server.lms.loans.dto.request.BookLoanRequest;
import com.server.lms.loans.dto.request.BookLoanSearchRequest;
import com.server.lms.loans.dto.request.RenewalRequest;
import com.server.lms.loans.dto.request.ReturnBookRequest;
import com.server.lms.loans.dto.response.BookLoanResponse;
import com.server.lms.loans.entity.BookLoan;
import com.server.lms.loans.enums.BookLoanState;
import com.server.lms.loans.exception.BookLoanException;
import com.server.lms.loans.mapper.BookLoanMapper;
import com.server.lms.loans.repository.BookLoanRepository;
import com.server.lms.loans.specification.BookLoanSpecification;
import com.server.lms.subscription.dto.response.SubscriptionResponse;
import com.server.lms.subscription.service.SubscriptionService;
import com.server.lms.user.entity.User;
import com.server.lms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final BookService bookService;
    private final BookLoanMapper bookLoanMapper;

    @Override
    public BookLoanResponse borrowBook(BookLoanRequest bookLoanRequest) {
        User user = userService.getCurrentUser();
        return borrowBookForUser(user, bookLoanRequest);
    }

    @Override
    public BookLoanResponse borrowBook(String userId, BookLoanRequest bookLoanRequest) {
        User user = userService.findEntityById(userId);  // throw exception if user not found
        return borrowBookForUser(user, bookLoanRequest);
    }

    private BookLoanResponse borrowBookForUser(User user, BookLoanRequest bookLoanRequest) {
        String userId = user.getId();

        // VALIDATION (User has active subscription & Book is available)
        SubscriptionResponse subscription = subscriptionService.getUserActiveSubscription(userId);

        Book book = bookService.findEntityById(bookLoanRequest.getBookId());

//        if (subscription)

        if (!book.getIsActive()) {
            throw new BookLoanException("Book is not active for borrowing");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new BookLoanException("Book is not available for borrowing");
        }

        // CHECK if user already borrow this book
        if (bookLoanRepository.hasBookAlreadyBorrowed(userId, book.getId())) {
            throw new BookLoanException("User already borrowed this book");
        }

        int availableLoansCount = bookLoanRepository.countActiveBookLoansByUser(userId);

        if (availableLoansCount >= subscription.getMaxBooksAllowed()) {
            throw new BookLoanException("User has reached the maximum number of books allowed for borrowing");
        }

        // CHECK for overdue books (user mustn't borrow new books until return the overdue ones)
        int overdueCount = bookLoanRepository.countOverdueBookLoansByUser(userId);

        if (overdueCount > 0) {
            throw new BookLoanException("User has overdue books");
        }

        // CREATE BOOK LOAN
        BookLoan bookLoan = BookLoan.builder()
                .user(user)
                .book(book)
                .bookLoanState(BookLoanState.BORROWED)
                .borrowingDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(Math.min(bookLoanRequest.getBorrowingDays(), subscription.getMaxBorrowingDays())))
                .renewalCount(0)
                .maxRenewals(subscription.getMaxRenewals())
                .isOverdue(false)
                .overdueDays(0)
                .notes(bookLoanRequest.getNotes())
                .build();

        // UPDATE BOOK AVAILABLE BOOKS

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return bookLoanMapper.toDTO(
                bookLoanRepository.save(bookLoan)
        );
    }

    @Override
    public BookLoanResponse returnBook(ReturnBookRequest returnBookRequest) {
        BookLoan bookLoan = findEntityById(returnBookRequest.getBookLoanId());

        if (!bookLoan.isBorrowed()) {
            throw new BookLoanException("Book loan is not currently borrowed");
        }

        bookLoan.setReturnDate(LocalDate.now());

        BookLoanState state = returnBookRequest.getBookLoanState();
        if (state == null)
            state = bookLoan.getDueDate().isBefore(LocalDate.now()) ? BookLoanState.OVERDUE : BookLoanState.RETURNED;
        bookLoan.setBookLoanState(state);

        bookLoan.setNotes(returnBookRequest.getNotes() == null ? "Book Returned By User" : returnBookRequest.getNotes());


        // TODO: FINE
        bookLoan.setOverdueDays(0);
        bookLoan.setIsOverdue(false);

        // Update Book Availability Again (IF RETURNED)
        if (!state.equals(BookLoanState.LOST)) {
            Book book = bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);

            // TODO: Process Next Reservation
        }


        bookLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDTO(bookLoan);
    }


    @Override
    public BookLoanResponse renewBorrow(RenewalRequest renewalRequest) {
        BookLoan bookLoan = findEntityById(renewalRequest.getBookLoanId());


        if (!bookLoan.canRenew()) {
            throw new BookLoanException("Book loan cannot be renewed");
        }

        SubscriptionResponse subscription = subscriptionService.getUserActiveSubscription(bookLoan.getUser().getId());

        bookLoan.setDueDate(bookLoan.getDueDate().plusDays(Math.min(renewalRequest.getExtensionDays(), subscription.getMaxBorrowingDays())));
        bookLoan.setRenewalCount(bookLoan.getRenewalCount() + 1);
        bookLoan.setNotes(renewalRequest.getNotes() == null ? "Book Loan Renewed By User" : renewalRequest.getNotes());

        bookLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDTO(bookLoan);
    }

    @Override
    public PageResponse<BookLoanResponse> getUserBookLoans(PageRequestDTO pageRequest, BookLoanState status) {
        User user = userService.getCurrentUser();
        Page<BookLoan> bookLoansPage;
        if (status != null) {
            bookLoansPage = bookLoanRepository.findByUserIdAndBookLoanState(user.getId(), status, pageRequest.generatePageable());
        } else {
            bookLoansPage = bookLoanRepository.findByUserId(user.getId(), pageRequest.generatePageable());
        }
        return PageResponse.<BookLoanResponse>builder()
                .content(bookLoansPage.map(bookLoanMapper::toDTO).getContent())
                .build()
                .setPageInfo(bookLoansPage);
    }


    @Override
    public PageResponse<BookLoanResponse> getAllBookLoans(BookLoanSearchRequest bookLoanSearchRequest) {
        Page<BookLoan> bookLoansPage;
        Specification<BookLoan> specification = BookLoanSpecification.buildSpecificationByBookLoanSearchRequest(bookLoanSearchRequest);

        if (specification == null) bookLoansPage = bookLoanRepository.findAll(bookLoanSearchRequest.generatePageable());
        else bookLoansPage = bookLoanRepository.findAll(specification, bookLoanSearchRequest.generatePageable());
        return PageResponse.<BookLoanResponse>builder()
                .content(bookLoansPage.map(bookLoanMapper::toDTO).getContent())
                .build()
                .setPageInfo(bookLoansPage);
    }

    @Override
    public Integer updateOverdueBookLoan() {
        int updateCount = 0;
        List<BookLoan> bookLoanList = bookLoanRepository.findAll(BookLoanSpecification.byOverdueOnly(LocalDate.now()));

        for (BookLoan bookLoan : bookLoanList) {
            if (bookLoan.getBookLoanState().equals(BookLoanState.BORROWED)) {
                bookLoan.setBookLoanState(BookLoanState.OVERDUE);
                bookLoan.setIsOverdue(true);
                int overdueDays = bookLoan.getDueDate().until(LocalDate.now()).getDays();
                bookLoan.setOverdueDays(overdueDays);
                bookLoanRepository.save(bookLoan);
                updateCount++;
            }
        }

        return updateCount;
    }

    @Override
    public boolean existsByUserIdAndBookIdAndStatus(String userId, String bookId, BookLoanState state) {
        return bookLoanRepository.existsByUserIdAndBookIdAndBookLoanState(userId, bookId, state);
    }

    @Override
    public BookLoan findEntityById(String id) {
        return bookLoanRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book loan not found with id " + id)
        );
    }
}
