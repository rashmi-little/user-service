package com.mindfire.backend.dto.response;

import com.mindfire.backend.entity.Role;

public record UserResponseDto(
        long id,
        String firstName,
        String lastName,
        String email,
        String role) {
}
