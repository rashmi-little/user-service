package com.mindfire.backend.dto.request;

import com.mindfire.backend.constants.ValidatorConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequestDto(
        @NotBlank(message = ValidatorConstants.FIRST_NAME_NOT_EMPTY)
        @Size(min = 3, max = 20, message = ValidatorConstants.FIELD_SIZE_FIRST_NAME)
        String firstName,

        @NotBlank(message = ValidatorConstants.LAST_NAME_NOT_EMPTY)
        @Size(min = 3, max = 20, message = ValidatorConstants.FIELD_SIZE_LAST_NAME)
        String lastName
) {
}
