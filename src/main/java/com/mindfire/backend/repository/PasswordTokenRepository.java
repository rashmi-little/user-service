package com.mindfire.backend.repository;

import com.mindfire.backend.entity.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    Optional<PasswordToken> findByToken(String token);

    List<PasswordToken> findByUserEmailAndIsUsedFalseAndExpirationTimeAfter(String email, LocalDateTime now);
}
