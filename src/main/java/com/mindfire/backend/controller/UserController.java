package com.mindfire.backend.controller;

import com.mindfire.backend.dto.request.ProfileRequestDto;
import com.mindfire.backend.dto.request.ResetPasswordRequestDto;
import com.mindfire.backend.dto.request.UserRequestDto;
import com.mindfire.backend.dto.response.PageResponse;
import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.service.PasswordTokenService;
import com.mindfire.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-service")
@Tag(name = "User Controller", description = "Handles user-related operations")
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Create User",
            description = "Creates a new user and returns the created user details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input provided", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User must be authenticated", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required permissions", content = @Content())
            }
    )
    @PostMapping("/user-data")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = userService.create(userRequestDto);

        return new ResponseEntity<UserResponseDto>(response, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Update User",
            description = "Updates an existing user based on ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User must be authenticated", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required permissions", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content())
            }
    )
    @PutMapping("/user/user-data/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable long id, @Valid @RequestBody ProfileRequestDto profileRequestDto) {
        UserResponseDto response = userService.update(id, profileRequestDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(
            summary = "Delete User",
            description = "Deletes a user based on ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User must be authenticated"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required permissions"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @DeleteMapping("/user-data/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        boolean isDeleted = userService.delete(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get User by ID",
            description = "Retrieves a user's details based on ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User must be authenticated", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required permissions", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content())
            }
    )
    @GetMapping("/user/user-data/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable long id) {
        UserResponseDto response = userService.getById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Get All Users",
            description = "Retrieves a list of all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User must be authenticated", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required permissions", content = @Content())
            }
    )
    @GetMapping("/user-data")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> response = userService.getAll();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Get Paginated Users",
            description = "Retrieves users with pagination support",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated users retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User must be authenticated", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required permissions", content = @Content())

            }
    )
    @GetMapping("/user-data/paginated")
    public ResponseEntity<PageResponse<UserResponseDto>> getAllUsersPaginated(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return new ResponseEntity<>(userService.getPaginatedUser(pageNumber, pageSize), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Current User Profile",
            description = "Retrieves the profile details of the currently authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Current user profile retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User must be authenticated", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required permissions", content = @Content())
            }
    )
    @GetMapping("/user/user-data/profile")
    public ResponseEntity<UserResponseDto> getCurrentUser(Principal principal) {
        UserResponseDto response = userService.getUserByEmail(principal.getName());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Reset User Password",
            description = "Resets the user's password using the provided reset token and new password.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Password reset successfully",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or expired token",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "token not found or user not found",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Old and new password conflict",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        log.info("reset password token {} new password {}", resetPasswordRequestDto.token(), resetPasswordRequestDto.newPassword());

        userService.changePassword(resetPasswordRequestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Password Reset Token",
            description = "Generates and returns a password reset token for the user identified by the provided email",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Password reset token generated successfully and can be used to reset the user's password",
                            content = @Content()),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request - Invalid email format or missing email parameter",
                            content = @Content()),
                    @ApiResponse(responseCode = "404",
                            description = "User not found - No account associated with the provided email",
                            content = @Content()),
                    @ApiResponse(responseCode = "500",
                            description = "Internal Server Error - An error occurred while generating the reset token",
                            content = @Content())
            }
    )
    @PostMapping("/reset-token")
    public ResponseEntity<Void> getResetPasswordToken(@Email @RequestParam String email) {
        String token = userService.getPasswordResetToken(email).getToken();

        log.info("The password register token is http://localhost:5173/reset-password?token={}", token);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
