package com.server.lms.loans.entity;

import com.server.lms.book.entity.Book;
import com.server.lms.loans.enums.BookLoanState;
import com.server.lms.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "book_loans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class
BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Book book;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookLoanState bookLoanState;

    @Column(nullable = false)
    private LocalDate borrowingDate;

    @Column(nullable = false)
    private LocalDate dueDate; // require due date

    private LocalDate returnDate; // actual due date


    @Column(nullable = false)
    private Integer renewalCount = 0; // number of re-loans

    @Column(nullable = false)
    private Integer maxRenewals = 2;

    @Column(length = 500)
    private String notes;


    @Column(nullable = false)
    private Boolean isOverdue = false; // deadline missed or not

    @Column(nullable = false)
    private Integer overdueDays = 0; // number of delay days (after deadline)

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate updatedAt;


    public boolean isBorrowed() {
        return bookLoanState == BookLoanState.BORROWED || bookLoanState == BookLoanState.OVERDUE;
    }

    public boolean canRenew() {
        return bookLoanState == BookLoanState.BORROWED
                && !isOverdue
                && renewalCount < maxRenewals;
    }

}
