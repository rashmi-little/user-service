package com.mindfire.backend.service;

import com.mindfire.backend.entity.PasswordToken;

import com.mindfire.backend.exception.TokenNotFoundException;
import com.mindfire.backend.exception.TokenExpiredException;

public interface PasswordTokenService {

    /**
     * Generates a new password reset token for the specified email address.
     * <p>
     * This method generates a new password reset token using the provided email, expires any existing tokens
     * associated with the email, and then saves the new token to the database.
     * </p>
     *
     * @param email the email address of the user for whom the password reset token is being generated
     * @return the {@link PasswordToken} object containing the generated token, now saved in the database
     */
    PasswordToken generateToken(String email);

    /**
     * Validates the provided password reset token.
     * <p>
     * This method checks if the token exists, if it has expired, or if it has already been used.
     * If any of these conditions are met, an exception is thrown. Otherwise, the valid token is returned.
     * </p>
     *
     * @param token the password reset token to be validated
     * @return the {@link PasswordToken} object if the token is valid
     * @throws TokenNotFoundException if the token is not found in the database
     * @throws TokenExpiredException if the token is expired or already used
     */
    PasswordToken validatePasswordResetToken(String token);

    /**
     * Expires all password reset tokens associated with the specified email.
     * <p>
     * This method retrieves all tokens for the given email and deletes them from the database.
     * </p>
     *
     * @param email the email address whose tokens are to be expired
     */
    void expireAllToken(String email);

    /**
     * Saves the provided {@link PasswordToken} to the database.
     * <p>
     * This method persists the given {@link PasswordToken} object in the repository, ensuring that the token
     * is stored for future validation or use during the password reset process.
     * </p>
     *
     * @param passwordToken the {@link PasswordToken} object to be saved in the database
     * @return the saved {@link PasswordToken} object after it has been persisted
     */
    PasswordToken save(PasswordToken passwordToken);
}
