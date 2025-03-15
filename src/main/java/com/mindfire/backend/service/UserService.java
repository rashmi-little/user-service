package com.mindfire.backend.service;

import com.mindfire.backend.dto.request.ProfileRequestDto;
import com.mindfire.backend.dto.request.UserRequestDto;
import com.mindfire.backend.dto.response.PageResponse;
import com.mindfire.backend.dto.response.UserResponseDto;

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

    void changePassword(long id, String newPassword);
}
