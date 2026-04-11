package com.server.lms.bookmark.service;

import com.server.lms.bookmark.dto.response.BookmarkResponse;
import com.server.lms.bookmark.entity.Bookmark;
import com.server.lms.bookmark.mapper.BookmarkMapper;
import com.server.lms.bookmark.repository.BookmarkRepository;
import com.server.lms._shared.dto.PageRequestDTO;
import com.server.lms._shared.dto.PageResponse;
import com.server.lms._shared.exception.DuplicateFieldException;
import com.server.lms.book.entity.Book;
import com.server.lms.book.service.BookService;
import com.server.lms.user.entity.User;
import com.server.lms.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper bookmarkMapper;
    private final BookService bookService;
    private final UserService userService;


    @Override
    public BookmarkResponse addToBookmarks(String bookId, String notes) {
        Book book = bookService.findEntityById(bookId); // validate & get
        User user = userService.getCurrentUser();

        if (bookmarkRepository.existsByUserIdAndBookId(user.getId(), bookId)) {
            throw new DuplicateFieldException("Bookmark already exists for this book");
        }

        Bookmark bookmark = Bookmark.builder()
                .book(book)
                .user(user)
                .notes(notes)
                .build();
        bookmark = bookmarkRepository.save(bookmark);

        return bookmarkMapper.toDTO(bookmark);
    }

    @Override
    @Transactional
    public void removeBookmark(String bookId) {
        User user = userService.getCurrentUser();
        System.out.println(bookmarkRepository.existsByUserIdAndBookId(user.getId(), bookId));
        if (!bookmarkRepository.existsByUserIdAndBookId(user.getId(), bookId)) {
            throw new DuplicateFieldException("Bookmark does not exist for this book id: " + bookId);
        }


        bookmarkRepository.deleteByUserIdAndBookId(user.getId(), bookId);
    }

    @Override
    public PageResponse<BookmarkResponse> getMyBookmarks(PageRequestDTO pageRequestDTO) {
        User user = userService.getCurrentUser();
        pageRequestDTO.setSortBy("addedAt");
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByUserId(user.getId(), pageRequestDTO.generatePageable());
        return PageResponse.<BookmarkResponse>builder()
                .content(bookmarkPage.map(bookmarkMapper::toDTO).getContent())
                .build()
                .setPageInfo(bookmarkPage);
    }

    @Override
    public Bookmark findEntityById(String id) {
        return bookmarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bookmark not found with id: " + id));

    }
}
