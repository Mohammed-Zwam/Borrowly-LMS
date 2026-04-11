package com.server.lms.bookmark.service;

import com.server.lms.bookmark.dto.response.BookmarkResponse;
import com.server.lms.bookmark.entity.Bookmark;
import com.server.lms._shared.base.BaseService;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;

public interface BookmarkService extends BaseService<Bookmark, String> {
    BookmarkResponse addToBookmarks(String bookId, String notes);

    void removeBookmark(String bookId);

    PageResponse<BookmarkResponse> getMyBookmarks(PageRequestDTO pageRequest);

}
