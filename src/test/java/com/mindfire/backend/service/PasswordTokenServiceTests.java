package com.mindfire.backend.service;

import com.mindfire.backend.constants.ValidatorConstants;
import com.mindfire.backend.entity.PasswordToken;
import com.mindfire.backend.exception.TokenExpiredException;
import com.mindfire.backend.exception.TokenNotFoundException;
import com.mindfire.backend.repository.PasswordTokenRepository;
import com.mindfire.backend.service.impl.PasswordTokenServiceImpl;
import com.mindfire.backend.utils.PasswordUtility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test class for password and token service")
public class PasswordTokenServiceTests {

    @Mock
    private PasswordTokenRepository passwordTokenRepository;

    @InjectMocks
    private PasswordTokenServiceImpl passwordTokenService;

    private PasswordToken passwordToken1;

    private PasswordToken passwordToken2;

    private List<PasswordToken> tokens;

    @BeforeEach
    void setup() {
        passwordToken1 = PasswordToken.builder()
                .id(1L)
                .userEmail("random@gmail.com")
                .createdTime(LocalDateTime.of(2025, 3, 20, 12, 0))
                .expirationTime(LocalDateTime.of(2025, 3, 27, 12, 0))
                .isUsed(false)
                .token("dummyToken1")
                .build();
        passwordToken2 = PasswordToken.builder()
                .id(2L)
                .userEmail("random@gmail.com")
                .createdTime(LocalDateTime.of(2025, 3, 20, 12, 0))
                .expirationTime(LocalDateTime.of(2025, 3, 27, 12, 5))
                .isUsed(false)
                .token("dummyToken2")
                .build();

        tokens = List.of(passwordToken1, passwordToken2);
    }

    @Test
    @DisplayName("save - should save password token")
    public void shouldSavePasswordToken() {
        // given -- condition or setup
        BDDMockito.given(passwordTokenRepository.save(passwordToken1)).willReturn(passwordToken1);
        // when -- action or behavior that are going to test
        passwordTokenService.save(passwordToken1);

        // then -- verify the output
        BDDMockito.verify(passwordTokenRepository, Mockito.times(1)).save(passwordToken1);
    }

    @Test
    @DisplayName("expireAllToken - should delete all the password reset token attach with given email")
    public void shouldExpireAllTokensForEmail() {
        String presentEmail = "random@gmail.com";
        // given -- condition or setup
        BDDMockito.given(passwordTokenRepository.findByUserEmail(presentEmail)).willReturn(tokens);

        // when -- action or behavior that are going to test
        passwordTokenService.expireAllToken(presentEmail);

        // then -- verify the output
        BDDMockito.verify(passwordTokenRepository, Mockito.times(1)).deleteAll(tokens);
    }

    @Test
    @DisplayName("generateToken - should call the passwordUtility and expire all token and then save the newly created token")
    public void generateToken() {
        // given -- condition or setup
        MockedStatic<PasswordUtility> staticMock = BDDMockito.mockStatic(PasswordUtility.class);
        BDDMockito.given(passwordTokenRepository.save(passwordToken1)).willReturn(passwordToken1);

        staticMock.when(() -> PasswordUtility.generateResetToken(passwordToken1.getUserEmail())).thenReturn(passwordToken1);

        // when -- action or behavior that are going to test
        PasswordToken newToken = passwordTokenService.generateToken(passwordToken1.getUserEmail());

        // then -- verify the output
        BDDMockito.verify(passwordTokenRepository, Mockito.times(1)).deleteAll(Mockito.any());
        BDDMockito.verify(passwordTokenRepository, Mockito.times(1)).save(newToken);
    }

    @Test
    @DisplayName("validatePasswordToken -(negative) should throw token not found exception if token not present")
    public void throwExceptionWhenTokenNotFound() {
        // given -- condition or setup
        String notPresentToken = "not present token";

        BDDMockito.given(passwordTokenRepository.findByToken(notPresentToken)).willReturn(Optional.empty());
        // when  and verify

        assertThatThrownBy(
                () -> {
                    passwordTokenService.validatePasswordResetToken(notPresentToken);
                }
        ).isInstanceOf(TokenNotFoundException.class).hasMessage(ValidatorConstants.INVALID_TOKEN);
    }

    @Test
    @DisplayName("validatePasswordToken -(negative) should throw tokenExpired exception if token is used")
    public void throwTokenExpiredWhenTokenIsUsed() {
        // given -- condition or setup
        PasswordToken usedToken = PasswordToken.builder()
                .token("random used token")
                .expirationTime(LocalDateTime.now().plusMinutes(5))
                .isUsed(true).build();

        BDDMockito.given(passwordTokenRepository.findByToken(usedToken.getToken())).willReturn(Optional.of(usedToken));

        // when  and verify

        assertThatThrownBy(
                () -> {
                    passwordTokenService.validatePasswordResetToken(usedToken.getToken());
                }
        ).isInstanceOf(TokenExpiredException.class).hasMessage(ValidatorConstants.TOKEN_EXPIRED);
    }

    @Test
    @DisplayName("validatePasswordToken -(negative) should throw tokenExpired exception if token expiration time reached")
    public void throwTokenExpiredWhenExpirationTimeReached() {
        // given -- condition or setup
        PasswordToken usedToken = PasswordToken.builder()
                .token("random used token")
                .expirationTime(LocalDateTime.now().minusSeconds(1))
                .isUsed(false).build();

        BDDMockito.given(passwordTokenRepository.findByToken(usedToken.getToken())).willReturn(Optional.of(usedToken));

        // when  and verify

        assertThatThrownBy(
                () -> {
                    passwordTokenService.validatePasswordResetToken(usedToken.getToken());
                }
        ).isInstanceOf(TokenExpiredException.class).hasMessage(ValidatorConstants.TOKEN_EXPIRED);
    }

    @Test
    @DisplayName("validatePasswordToken - (positive) return the passwordTokenObject if token is valid")
    public void shouldReturnPasswordTokenWhenTokenIsValid() {
        // given -- condition or setup
        BDDMockito.given(passwordTokenRepository.findByToken(passwordToken2.getToken())).willReturn(Optional.of(passwordToken2));

        // when -- action or behavior that are going to test
        PasswordToken validToken = passwordTokenService.validatePasswordResetToken(passwordToken2.getToken());

        // then -- verify the output
        assertThat(validToken).isNotNull();
        assertThat(validToken).isEqualTo(passwordToken2);
    }

}
