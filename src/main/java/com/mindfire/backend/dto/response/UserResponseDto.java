package com.mindfire.backend.dto.response;

import com.mindfire.backend.enums.Role;

public record UserResponseDto(
        String userName,
        String email,
        Role role) {
}
