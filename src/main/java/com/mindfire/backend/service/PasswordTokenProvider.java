package com.mindfire.backend.service;

import com.mindfire.backend.entity.PasswordToken;

import java.time.LocalDateTime;

public interface PasswordTokenProvider {
    /**
     * Generates and saves a new password reset token for the specified email address.
     * <p>
     * This method calls a utility function to generate a new reset token for the given email,
     * expires any existing tokens associated with that email, and then saves the new token
     * in the repository.
     * </p>
     *
     * @param email The email address for which the reset token is generated.
     * @return A {@link PasswordToken} object containing the newly generated token and related information.
     */
    PasswordToken generateToken(String email);

    /**
     * Validates a password reset token by checking its existence, expiration, and usage status.
     * <p>
     * This method retrieves the password reset token from the repository using the provided token string.
     * It checks if the token exists, has not expired, and has not already been used. If any of these conditions
     * are not met, an exception is thrown. If the token is valid, it is marked as used and saved back to the repository.
     * </p>
     *
     * @param token The password reset token to be validated.
     * @return A {@link PasswordToken} object that has been validated and marked as used.
     * @throws RuntimeException if the token is not found, expired, or already used.
     */
    public PasswordToken validatePasswordResetToken(String token);

    /**
     * Expires all unused password reset tokens for the specified email address that have not yet expired.
     * <p>
     * This method finds all unused tokens associated with the given email address that have an
     * expiration time later than the specified time. It marks these tokens as "used" and saves
     * the updated tokens back to the repository.
     * </p>
     *
     * @param email The email address for which the tokens should be expired.
     * @param time The current time used to check which tokens are still valid.
     */
    public void expireAllToken(String email, LocalDateTime time);
}
