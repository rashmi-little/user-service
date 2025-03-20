package com.mindfire.backend.service.impl;

import com.mindfire.backend.constants.ValidatorConstants;
import com.mindfire.backend.dto.request.ProfileRequestDto;
import com.mindfire.backend.dto.request.ResetPasswordRequestDto;
import com.mindfire.backend.entity.PasswordToken;
import com.mindfire.backend.entity.Role;
import com.mindfire.backend.exception.SamePasswordException;
import com.mindfire.backend.exception.UserNotFoundException;
import com.mindfire.backend.kafka.UserKafkaProducer;
import com.mindfire.backend.dto.request.UserRequestDto;
import com.mindfire.backend.dto.response.PageResponse;
import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.entity.User;
import com.mindfire.backend.mapper.MapHelper;
import com.mindfire.backend.repository.UserRepository;
import com.mindfire.backend.service.PasswordTokenService;
import com.mindfire.backend.service.RoleService;
import com.mindfire.backend.service.UserService;
import com.mindfire.basedomains.dto.UserRegistrationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final RoleService roleService;

	private final PasswordTokenService passwordTokenService;

	private static final String ROLE_USER = "USER";

	private final UserKafkaProducer userKafkaProducer;

	@Override
	public UserResponseDto create(UserRequestDto userRequestDto) {
		User user = MapHelper.mapToUser(userRequestDto);

		Role role = roleService.getRoleByName(ROLE_USER);
		user.setRole(role);

		User savedUser = userRepository.save(user);

		// helps to generate token based on the user email
		PasswordToken savedToken = passwordTokenService.generateToken(user.getEmail());

		log.info("The password register token is http://localhost:5173/reset-password?token={}", savedToken.getToken());

		String verificationLink = "http://localhost:5173/password-reset?token=" + savedToken.getToken();

		UserRegistrationEvent userRegistrationEvent = new UserRegistrationEvent(user.getEmail(), verificationLink);
		userKafkaProducer.publishUserRegistrationEvent(userRegistrationEvent);

		return MapHelper.mapToUserResponse(savedUser);
	}

	@Override
	public UserResponseDto update(long id, ProfileRequestDto profileRequestDto) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ValidatorConstants.USER_ID_NOT_FOUND));

		BeanUtils.copyProperties(profileRequestDto, user);

		userRepository.save(user);
		return MapHelper.mapToUserResponse(user);
	}

	@Override
	public boolean delete(long id) {
		if (userRepository.findById(id).isEmpty()) {
			return false;
		}

		userRepository.deleteById(id);

		return true;
	}

	@Override
	public UserResponseDto getById(long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ValidatorConstants.USER_ID_NOT_FOUND));

		return MapHelper.mapToUserResponse(user);
	}

	@Override
	public List<UserResponseDto> getAll() {

		return userRepository.findAll().stream().map(MapHelper::mapToUserResponse).collect(Collectors.toList());
	}

	@Override
	public PageResponse<UserResponseDto> getPaginatedUser(int pageNumber, int pageSize) {
		if (pageNumber <= 0) {
			throw new RuntimeException(ValidatorConstants.INVALID_PAGE_SIZE);
		}

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

		Page<User> pageData = userRepository.findAll(pageable);

		List<UserResponseDto> userResponse = pageData.getContent().stream().map(MapHelper::mapToUserResponse).toList();

		return new PageResponse<UserResponseDto>(userResponse, pageData.getTotalPages(), pageData.getTotalElements(),
				pageData.getSize(), pageData.getNumber());
	}

	@Override
	public UserResponseDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(ValidatorConstants.USER_EMAIL_NOT_FOUND));

		return MapHelper.mapToUserResponse(user);
	}

	@Override
	public void changePassword(ResetPasswordRequestDto resetPasswordRequestDto) {
		String token = resetPasswordRequestDto.token();
		String newPassword = resetPasswordRequestDto.newPassword();

		PasswordToken passwordToken = passwordTokenService.validatePasswordResetToken(token);
		User user = userRepository.findByEmail(passwordToken.getUserEmail())
				.orElseThrow(() -> new UserNotFoundException(ValidatorConstants.USER_EMAIL_NOT_FOUND));

		if ((user.getPassword() != null && !user.getPassword().isEmpty())
				&& passwordEncoder.matches(newPassword, user.getPassword())) {
			throw new SamePasswordException(ValidatorConstants.SAME_PASSWORD_ERROR_MESSAGE);
		}

		user.setPassword(passwordEncoder.encode(newPassword));

		userRepository.save(user);

		passwordToken.setUsed(true);

		passwordTokenService.save(passwordToken);
	}

	@Override
	public PasswordToken getPasswordResetToken(String email) {
		// Check for unregister email
		userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(ValidatorConstants.UNREGISTERED_USER));

		return passwordTokenService.generateToken(email);
	}
}
