package com.server.lms.user.repository;

import com.server.lms._shared.base.BaseRepository;
import com.server.lms.user.entity.PasswordResetToken;
import com.server.lms.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByToken(String token);

}
