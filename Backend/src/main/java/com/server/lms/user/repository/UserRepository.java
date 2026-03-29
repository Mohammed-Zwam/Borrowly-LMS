package com.server.lms.user.repository;

import com.server.lms._shared.base.BaseRepository;
import com.server.lms.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, String> {

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(String userId, LocalDateTime lastLogin);

    Optional<User> findByEmail(String email);
}
