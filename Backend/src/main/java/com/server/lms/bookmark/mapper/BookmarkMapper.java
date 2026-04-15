package com.server.lms.bookmark.mapper;

import com.server.lms.bookmark.dto.response.BookmarkResponse;
import com.server.lms.bookmark.entity.Bookmark;
import com.server.lms._shared.base.BaseMapper;
import com.server.lms.book.mapper.BookMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = BookMapper.class
)
public abstract class BookmarkMapper extends BaseMapper<Object, BookmarkResponse, Bookmark> {
    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    public abstract BookmarkResponse toDTO(Bookmark bookmark);
}
