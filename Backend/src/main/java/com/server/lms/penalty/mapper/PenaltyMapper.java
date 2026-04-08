package com.server.lms.penalty.mapper;

import com.server.lms._shared.base.BaseMapper;
import com.server.lms.penalty.dto.request.PenaltyRequest;
import com.server.lms.penalty.dto.response.PenaltyResponse;
import com.server.lms.penalty.entity.Penalty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class PenaltyMapper extends BaseMapper<PenaltyRequest, PenaltyResponse, Penalty> {

    @Override
    @Mapping(target = "bookLoanId", source = "bookLoan.id")
    @Mapping(target = "bookTitle", source = "bookLoan.book.title")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "cancellationByUserId", source = "cancelledBy.id")
    @Mapping(target = "cancellationByUserName", source = "cancelledBy.name")
    @Mapping(target = "paidByUserId", source = "paidBy.id")
    @Mapping(target = "paidByUserName", source = "paidBy.name")
    public abstract PenaltyResponse toDTO(Penalty penalty);
}
