package com.mindfire.backend.service;

import com.mindfire.backend.dto.request.ProfileRequestDto;
import com.mindfire.backend.dto.request.ResetPasswordRequestDto;
import com.mindfire.backend.dto.request.UserRequestDto;
import com.mindfire.backend.dto.response.PageResponse;
import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.entity.PasswordToken;
import com.mindfire.backend.exception.UserNotFoundException;
import com.mindfire.backend.exception.SamePasswordException;

import java.util.List;

public interface UserService {
    /**
     * Creates a new user.
     *
     * @return UserResponseDto containing the created user's details.
     */
    UserResponseDto create(UserRequestDto userRequestDto);

    /**
     * Updates an existing user's details.
     *
     * @param id                user id
     * @param profileRequestDto profile related information
     * @return UserResponseDto containing the updated user's details.
     */
    UserResponseDto update(long id, ProfileRequestDto profileRequestDto);

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to be deleted.
     * @return boolean indicating whether the deletion was successful.
     */
    boolean delete(long id);

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return UserResponseDto containing the user's details.
     */
    UserResponseDto getById(long id);

    /**
     * Retrieves all users.
     *
     * @return A list of UserResponseDto containing the details of all users.
     */
    List<UserResponseDto> getAll();

    /**
     * Retrieves all users in page wise
     *
     * @param pageNumber current page number
     * @param pageSize   size of each page
     * @return A page of UserResponseDto containing the details of all users.
     */
    PageResponse<UserResponseDto> getPaginatedUser(int pageNumber, int pageSize);

    /**
     * @param email take the user email
     * get the user response details by user email
     * @return UserResponseDto
     */
    UserResponseDto getUserByEmail(String email);

    /**
     * Changes the password of the user based on the provided reset password request.
     * <p>
     * This method validates the password reset token, checks if the new password is different from the current one,
     * and then updates the user's password in the database. If the user has an existing password that matches
     * the new password, a conflict exception is thrown.
     * </p>
     *
     * @param resetPasswordRequestDto the {@link ResetPasswordRequestDto} containing the password reset token and the new password
     * @throws UserNotFoundException if the user associated with the password reset token is not found
     * @throws SamePasswordException if the new password is the same as the old password
     */
    void changePassword(ResetPasswordRequestDto resetPasswordRequestDto);

    /**
     * Retrieves the password reset token for a given email address.
     * <p>
     * This method generates a password reset token associated with the specified email address.
     * The token is used for resetting the user's password.
     * </p>
     *
     * @param email the email address of the user requesting a password reset
     * @return a {@link PasswordToken} containing the token details associated with the specified email
     * @throws UserNotFoundException if no user is found with the provided email address
     */
    PasswordToken getPasswordResetToken(String email);
    
    long countTotalUser();
}
