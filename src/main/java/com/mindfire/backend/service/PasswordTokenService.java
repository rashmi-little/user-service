package com.mindfire.backend.service;

import com.mindfire.backend.entity.PasswordToken;

public interface PasswordTokenService {
    void changePassword(String token, String newPassword);

    PasswordToken savePasswordToken(PasswordToken token);
}
