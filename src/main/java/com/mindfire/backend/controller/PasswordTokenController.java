package com.mindfire.backend.controller;

import com.mindfire.backend.dto.request.ResetPasswordRequestDto;
import com.mindfire.backend.service.PasswordTokenProvider;
import com.mindfire.backend.service.PasswordTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password-service")
@Slf4j
@Tag(name = "password and token Controller", description = "Handles password-token related operations")
public class PasswordTokenController {

    private final PasswordTokenProvider passwordTokenProvider;

    private final PasswordTokenService passwordTokenService;

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        log.info("reset password token {} new password {}", resetPasswordRequestDto.token(), resetPasswordRequestDto.newPassword());
        passwordTokenService.changePassword(resetPasswordRequestDto.token(), resetPasswordRequestDto.newPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // we will not be sending the reset token to the frontend we will be sending
    // token to the registered mail address

    @PostMapping("/reset-token")
    public ResponseEntity<String> getResetPasswordToken(@RequestParam String email) {
        String token = passwordTokenProvider.generateToken(email).getToken();

        return ResponseEntity.ok(token);
    }
}
