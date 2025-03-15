package com.mindfire.backend.dto.request;

import com.mindfire.backend.constants.ValidatorConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDto(
        @NotBlank(message = ValidatorConstants.TOKEN_NOT_EMPTY)
        String token,

        @NotBlank(message = ValidatorConstants.PASSWORD_NOT_EMPTY)
        @Size(min = 6, message = ValidatorConstants.PASSWORD_SIZE)
        String newPassword) {
}
