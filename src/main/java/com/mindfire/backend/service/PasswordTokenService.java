package com.mindfire.backend.service;

import com.mindfire.backend.entity.PasswordToken;

public interface PasswordTokenService {

    /**
     * Changes the user's password using a valid password reset token.
     * <p>
     * This method first validates the provided password reset token. If the token is valid,
     * it retrieves the associated user's email, fetches the user's details, and then updates
     * the user's password to the provided new password.
     * </p>
     *
     * @param token       The password reset token used to verify the user's identity.
     * @param newPassword The new password to set for the user.
     * @throws RuntimeException if the token is invalid or any other error occurs during the password change process.
     */
    void changePassword(String token, String newPassword);
}
