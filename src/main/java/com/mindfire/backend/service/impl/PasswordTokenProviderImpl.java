package com.mindfire.backend.service.impl;

import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.entity.PasswordToken;
import com.mindfire.backend.repository.PasswordTokenRepository;
import com.mindfire.backend.service.PasswordTokenProvider;
import com.mindfire.backend.utils.PasswordUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordTokenProviderImpl implements PasswordTokenProvider {
    private final PasswordTokenRepository passwordTokenRepository;

    @Override
    public PasswordToken generateToken(String email) {
        // For invalid email no need to process the email

        PasswordToken token = PasswordUtility.generateResetToken(email);

        expireAllToken(email, LocalDateTime.now());

        passwordTokenRepository.save(token);
        return token;
    }

    public PasswordToken validatePasswordResetToken(String token) {
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("No token found in the db"));

        if (passwordToken.getExpirationTime().isBefore(LocalDateTime.now()) || passwordToken.isUsed()) {
            throw new RuntimeException("invalid passwordResetToken");
        }

        passwordToken.setUsed(true);

        PasswordToken savedToken = passwordTokenRepository.save(passwordToken);

        return savedToken;
    }

    public void expireAllToken(String email, LocalDateTime time) {
        List<PasswordToken> tokens = passwordTokenRepository.findByUserEmailAndIsUsedFalseAndExpirationTimeAfter(email, time);

        List<PasswordToken> updatedTokens = tokens.stream().map((token) -> {
            token.setUsed(true);
            return token;
        }).collect(Collectors.toList());

        passwordTokenRepository.saveAll(updatedTokens);
    }
}
