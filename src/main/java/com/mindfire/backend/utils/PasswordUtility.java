package com.mindfire.backend.utils;

import com.mindfire.backend.entity.PasswordToken;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordUtility {

    /**
     * Generates a password reset token for the specified email address.
     * <p>
     * This method creates a new random UUID as the reset token, and it associates the token
     * with the provided email. It also sets the creation time, expiration time (5 minutes from creation),
     * and marks the token as unused.
     * </p>
     *
     * @param email The email address for which the reset token is generated.
     * @return A {@link PasswordToken} object containing the generated token and related information.
     */
    public static PasswordToken generateResetToken(String email) {
        String randomUuidToken = UUID.randomUUID().toString();

        PasswordToken token = PasswordToken.builder().token(randomUuidToken).userEmail(email).createdTime(LocalDateTime.now()).expirationTime(LocalDateTime.now().plusMinutes(5)).isUsed(false).build();
        return token;
    }
}
