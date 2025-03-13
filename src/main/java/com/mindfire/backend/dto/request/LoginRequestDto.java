package com.mindfire.backend.dto.request;

import com.mindfire.backend.constants.ValidatorConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = ValidatorConstants.EMAIL_NOT_EMPTY)
        @Email(message = ValidatorConstants.EMAIL_INVALID)
        String email,

        @NotBlank(message = ValidatorConstants.PASSWORD_NOT_EMPTY)
        @Size(min = 6, message = ValidatorConstants.PASSWORD_SIZE)
        String password) {
}
