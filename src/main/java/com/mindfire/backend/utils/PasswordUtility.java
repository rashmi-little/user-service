package com.mindfire.backend.utils;

import com.mindfire.backend.entity.PasswordToken;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordUtility {
    public static PasswordToken generateResetToken(String email) {
        String randomUuidToken = UUID.randomUUID().toString();

        PasswordToken token = PasswordToken.builder().token(randomUuidToken).userEmail(email)
                .createdTime(LocalDateTime.now())
                .expirationTime(LocalDateTime.now().plusMinutes(5))
                .isUsed(false)
                .build();
        return token;
    }
}
