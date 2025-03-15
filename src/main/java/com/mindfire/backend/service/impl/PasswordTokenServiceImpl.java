package com.mindfire.backend.service.impl;

import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.entity.PasswordToken;
import com.mindfire.backend.repository.PasswordTokenRepository;
import com.mindfire.backend.service.PasswordTokenProvider;
import com.mindfire.backend.service.PasswordTokenService;
import com.mindfire.backend.service.UserService;
import com.mindfire.backend.utils.PasswordUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordTokenServiceImpl implements PasswordTokenService {

    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordTokenProvider passwordTokenProvider;
    private final UserService userService;

    @Override
    public void changePassword(String token, String newPassword) {
        PasswordToken savedTokenObject = passwordTokenProvider.validatePasswordResetToken(token);

        UserResponseDto userResponseDto = userService.getUserByEmail(savedTokenObject.getUserEmail());

        userService.changePassword(userResponseDto.id(), newPassword);
    }

    // currently it will generate the token after mail
    // it will simply said in frontend if it registered you will get the link



    @Override
    public PasswordToken savePasswordToken(PasswordToken token) {
        return passwordTokenRepository.save(token);
    }
}
