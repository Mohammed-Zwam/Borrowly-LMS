package com.server.lms.bookmark.repository;

import com.server.lms.bookmark.entity.Bookmark;
import com.server.lms._shared.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends BaseRepository<Bookmark, String> {

    Page<Bookmark> findByUserId(String userId, Pageable pageable);

    boolean existsByUserIdAndBookId(String userId, String bookId);


    @Modifying
    void deleteByUserIdAndBookId(String userId, String bookId);
}
