package com.mindfire.backend.service;

import com.mindfire.backend.entity.PasswordToken;

import java.time.LocalDateTime;

public interface PasswordTokenProvider {
    PasswordToken generateToken(String email);
    public PasswordToken validatePasswordResetToken(String token);
    public void expireAllToken(String email, LocalDateTime time);
}
