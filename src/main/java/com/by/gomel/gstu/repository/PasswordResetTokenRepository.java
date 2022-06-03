package com.by.gomel.gstu.repository;

import com.by.gomel.gstu.model.PasswordResetToken;
import com.by.gomel.gstu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken getPasswordResetTokenByToken(String token);

    @Query("SELECT user from PasswordResetToken where token=?1")
    User getUserByToken(String token);
}
