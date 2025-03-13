package com.mindfire.backend.mapper;

import com.mindfire.backend.dto.request.UserRequestDto;
import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.entity.User;
import org.springframework.beans.BeanUtils;

/**
 * A utility class for mapping between different objects
 * This class provides static methods to convert between these objects.
 */
public class MapHelper {

    /**
     * Maps a {@link UserRequestDto} to a {@link User} entity.
     * @param userRequestDto the DTO containing user request data
     * @return a new {@link User} entity populated with data from the given DTO
     */
    public static User mapToUser(UserRequestDto userRequestDto) {
        User user = new User();

        BeanUtils.copyProperties(userRequestDto, user);

        return user;
    }

    /**
     * Maps a {@link User} entity to a {@link UserResponseDto}.
     * @param user the {@link User} entity to be converted to a response DTO
     * @return a new {@link UserResponseDto} containing the user data from the entity
     */
    public static UserResponseDto mapToUserResponse(User user) {
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(),user.getEmail(), user.getRole().getName());
    }
}
