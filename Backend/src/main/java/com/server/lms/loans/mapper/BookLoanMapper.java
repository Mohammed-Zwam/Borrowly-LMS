package com.server.lms.loans.mapper;


import com.server.lms._shared.base.BaseMapper;
import com.server.lms.loans.dto.request.BookLoanRequest;
import com.server.lms.loans.dto.response.BookLoanResponse;
import com.server.lms.loans.entity.BookLoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class BookLoanMapper extends BaseMapper<BookLoanRequest, BookLoanResponse, BookLoan> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookIsbn", source = "book.isbn")
    @Mapping(target = "bookCoverImage", source = "book.coverImageUrl")
    @Mapping(
            target = "remainingDays",
            expression = "java(Math.max(java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), bookLoan.getDueDate()), 0))"
    )
    public abstract BookLoanResponse toDTO(BookLoan bookLoan);

}
