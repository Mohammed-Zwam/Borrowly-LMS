package com.server.lms.reviews.mapper;


import com.server.lms._shared.base.BaseMapper;
import com.server.lms.reviews.dto.request.UpdateBookReviewRequest;
import com.server.lms.reviews.dto.response.BookReviewResponse;
import com.server.lms.reviews.entity.BookReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class BookReviewMapper extends BaseMapper<Object, BookReviewResponse, BookReview> {
    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    public abstract BookReviewResponse toDTO(BookReview bookReview);

    public abstract BookReview update(UpdateBookReviewRequest request, @MappingTarget BookReview bookReview);
}
