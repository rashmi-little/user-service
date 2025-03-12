package com.mindfire.backend.controller;

import com.mindfire.backend.dto.request.LoginRequestDto;
import com.mindfire.backend.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.userName(),
                loginRequestDto.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return ResponseEntity.ok(jwtTokenProvider.generateToken(authentication));
    }
}
