package com.mindfire.backend.dto.request;

import com.mindfire.backend.constants.ValidatorConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = ValidatorConstants.USERNAME_NOT_EMPTY)
        @Size(min = 3, max = 20, message = ValidatorConstants.USERNAME_SIZE)
        String userName,

        @NotBlank(message = ValidatorConstants.PASSWORD_NOT_EMPTY)
        @Size(min = 6, message = ValidatorConstants.PASSWORD_SIZE)
        String password) {
}
