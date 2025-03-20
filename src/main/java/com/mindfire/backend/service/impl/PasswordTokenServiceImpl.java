package com.mindfire.backend.service.impl;

import com.mindfire.backend.constants.ValidatorConstants;
import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.entity.PasswordToken;
import com.mindfire.backend.exception.TokenExpiredException;
import com.mindfire.backend.exception.TokenNotFoundException;
import com.mindfire.backend.exception.UserNotFoundException;
import com.mindfire.backend.repository.PasswordTokenRepository;
import com.mindfire.backend.repository.UserRepository;
import com.mindfire.backend.service.PasswordTokenService;
import com.mindfire.backend.service.UserService;
import com.mindfire.backend.utils.PasswordUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordTokenServiceImpl implements PasswordTokenService {

    private final PasswordTokenRepository passwordTokenRepository;

    @Override
    public PasswordToken generateToken(String email) {
        PasswordToken token = PasswordUtility.generateResetToken(email);

        expireAllToken(email);

        return passwordTokenRepository.save(token);
    }

    @Override
    public PasswordToken validatePasswordResetToken(String token) {
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException(ValidatorConstants.INVALID_TOKEN));

        if (passwordToken.getExpirationTime().isBefore(LocalDateTime.now()) || passwordToken.isUsed()) {
            throw new TokenExpiredException(ValidatorConstants.TOKEN_EXPIRED);
        }

        return passwordToken;
    }

    @Override
    public void expireAllToken(String email) {
        List<PasswordToken> tokens = passwordTokenRepository.findByUserEmail(email);
        passwordTokenRepository.deleteAll(tokens);
    }

    @Override
    public PasswordToken save(PasswordToken passwordToken) {
        return passwordTokenRepository.save(passwordToken);
    }
}
