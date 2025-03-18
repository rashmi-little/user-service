package com.mindfire.backend.service;

import com.mindfire.backend.dto.request.ProfileRequestDto;
import com.mindfire.backend.dto.request.UserRequestDto;
import com.mindfire.backend.dto.response.PageResponse;
import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.exception.UserNotFoundException;

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
     * Changes the password for a user identified by their ID.
     * <p>
     * This method first checks if the user exists by searching for their ID. If the user is not found,
     * a {@link UserNotFoundException} is thrown. If the user's current password is set and matches the new password,
     * a {@link RuntimeException} is thrown to prevent the user from setting the same password. If the new password is valid,
     * it is encoded and saved to the user's account.
     * </p>
     *
     * @param id The ID of the user whose password is to be changed.
     * @param newPassword The new password to set for the user.
     * @throws UserNotFoundException if no user is found with the given ID.
     * @throws RuntimeException if the new password is the same as the old password.
     */
    void changePassword(long id, String newPassword);
}
