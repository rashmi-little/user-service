package com.mindfire.backend.service;

import com.mindfire.backend.constants.ValidatorConstants;
import com.mindfire.backend.dto.request.ProfileRequestDto;
import com.mindfire.backend.dto.request.UserRequestDto;
import com.mindfire.backend.dto.response.PageResponse;
import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.entity.Role;
import com.mindfire.backend.entity.User;
import com.mindfire.backend.exception.UserNotFoundException;
import com.mindfire.backend.repository.UserRepository;
import com.mindfire.backend.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test class for user service")
@Slf4j
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private Role role;

    private List<User> users;

    @BeforeEach
    void setup() {
        user = User.builder().id(1L).email("rrn@gmail.com").role(Role.builder().id(1L).name("USER").build()).firstName("Rashmi").lastName("Nayak").build();

        users = List.of(user, User.builder().id(2L).email("latesh@gmail.com").role(Role.builder().id(2L).name("ADMIN").build()).firstName("Latesh").lastName("lokanatham").build(), User.builder().id(3L).email("kirti@gmail.com").role(Role.builder().id(3L).name("USER").build()).firstName("kirti").lastName("Mishra").build());

        role = Role.builder().id(1L).name("USER").build();
    }

    @Test
    @DisplayName("delete -(negative) return false if user not found to delete")
    public void returnFalseIfUserNotFound() {
        long notPresentId = 6;
        // given -- condition or setup
        BDDMockito.given(userRepository.findById(notPresentId)).willReturn(Optional.empty());

        // when -- action or behavior that are going to test
        boolean response = userService.delete(notPresentId);

        // then -- verify the output
        assertThat(response).isFalse();
        // if return false then must not call the deleteById method
        BDDMockito.verify(userRepository, Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    @DisplayName("delete -(positive) return true if successfully delete user")
    public void return_true_if_employee_deleted_successfully() {
        long presentId = user.getId();

        // given -- condition or setup
        BDDMockito.given(userRepository.findById(presentId)).willReturn(Optional.of(user));

        // when -- action or behavior that are going to test
        boolean response = userService.delete(presentId);

        // then -- verify the output
        assertThat(response).isTrue();
        // delete by id should called if the user is present
        BDDMockito.verify(userRepository, Mockito.times(1)).deleteById(presentId);
    }

    @Test
    @DisplayName("getById -(negative) throw exception UserNotFound when user id not present")
    public void throwExceptionUserNotFoundWhenIdNotPresent() {
        long notPresentId = 6;
        // given -- condition or setup
        BDDMockito.given(userRepository.findById(notPresentId)).willReturn(Optional.empty());

        // check
        assertThatThrownBy(() -> {
            userService.getById(notPresentId);
        }).isInstanceOf(UserNotFoundException.class)
                .hasMessage(ValidatorConstants.USER_ID_NOT_FOUND);

    }

    @Test
    @DisplayName("getById -(positive) return user data when user id found")
    public void returnUserWhenUserIdFound() {
        // given -- condition or setup
        BDDMockito.given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // when -- action or behavior that are going to test
        UserResponseDto response = userService.getById(user.getId());

        // then -- verify the output
        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(UserResponseDto.class);
        assertThat(response.email()).isEqualTo(user.getEmail());
        assertThat(response.role()).isEqualTo(user.getRole().getName());
        assertThat(response.firstName()).isEqualTo(user.getFirstName());
        assertThat(response.lastName()).isEqualTo(user.getLastName());
    }

    @Test
    @DisplayName("getAll -(negative) return empty list when no user present")
    public void returnEmptyListWhenNoUserFound() {
        // given -- condition or setup
        BDDMockito.given(userRepository.findAll()).willReturn(Collections.emptyList());

        // when -- action or behavior that are going to test
        List<UserResponseDto> response = userService.getAll();

        // then -- verify the output
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("getAll -(positive) return list of user when users are present")
    public void returnUserListWhenUsersFound() {
        // given -- condition or setup
        BDDMockito.given(userRepository.findAll()).willReturn(users);

        // when -- action or behavior that are going to test
        List<UserResponseDto> response = userService.getAll();

        // then -- verify the output
        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        assertThat(response).hasSize(users.size());
    }

    @Test
    @DisplayName("getUserByEmail -(negative) throws exception when UserNotFoundException with the given email")
    public void throwsExceptionWhenUserEmailNotFound() {
        String notPresentEmail = "notavailable@gmail.com";
        // given -- condition or setup
        BDDMockito.given(userRepository.findByEmail(notPresentEmail)).willReturn(Optional.empty());

        // check
        assertThatThrownBy(() -> {
            userService.getUserByEmail(notPresentEmail);
        }).isInstanceOf(UserNotFoundException.class).hasMessage(ValidatorConstants.USER_EMAIL_NOT_FOUND);
    }

    @Test
    @DisplayName("getUserByEmail -(positive) return user data of the respective email")
    public void returnUserWhenUserEmailFound() {
        String presentEmail = user.getEmail();
        // given -- condition or setup
        BDDMockito.given(userRepository.findByEmail(presentEmail)).willReturn(Optional.of(user));

        // when -- action or behavior that are going to test
        UserResponseDto response = userService.getUserByEmail(presentEmail);

        // then -- verify the output
        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(UserResponseDto.class);
        assertThat(response.email()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Update -(negative) when user not found with id")
    public void throwsExceptionWhenUserIdNotFound() {
        ProfileRequestDto profileRequestDto = new ProfileRequestDto("siva", "Gedela");
        long notPresentId = 6;
        // given -- condition or setup
        BDDMockito.given(userRepository.findById(notPresentId)).willReturn(Optional.empty());

        // check
        assertThatThrownBy(() -> {
            userService.update(notPresentId, profileRequestDto);
        }).isInstanceOf(UserNotFoundException.class).hasMessage(ValidatorConstants.USER_ID_NOT_FOUND);

        // once the exception is thorwn the save method should not be invoked
        BDDMockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Update -(positive) check if the save method is called with proper field that are copied from profile request dto")
    public void perfectlyCopyFromProfileDtoToUser() {
        ProfileRequestDto profileRequestDto = new ProfileRequestDto("siva", "Gedela");
        long presentId = user.getId();
        // given -- condition or setup
        BDDMockito.given(userRepository.findById(presentId)).willReturn(Optional.of(user));

        // when -- action or behavior that are going to test
        userService.update(presentId, profileRequestDto);
        // then -- verify the output
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        BDDMockito.verify(userRepository, Mockito.times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        log.info("Capture value is {}", capturedUser);

        assertThat(capturedUser).isInstanceOf(User.class);
        assertThat(capturedUser.getFirstName()).isEqualTo(profileRequestDto.firstName());
        assertThat(capturedUser.getLastName()).isEqualTo(profileRequestDto.lastName());
    }

    @Test
    @DisplayName("Update -(positive) when user found with id check and update success we are getting the updated value")
    public void returnUpdateUserResponseWhenUserFound() {
        ProfileRequestDto profileRequestDto = new ProfileRequestDto("siva", "Gedela");
        long presentId = user.getId();
        // given -- condition or setup
        BDDMockito.given(userRepository.findById(presentId)).willReturn(Optional.of(user));

        // when -- action or behavior that are going to test
        UserResponseDto response = userService.update(presentId, profileRequestDto);
        // then -- verify the output
        BDDMockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(UserResponseDto.class);
        assertThat(response.firstName()).isEqualTo(profileRequestDto.firstName());
        assertThat(response.lastName()).isEqualTo(profileRequestDto.lastName());
    }

    @Test
    @DisplayName("getPaginatedUser -(negative) throws exception for invalid page")
    public void shouldThrowExceptionWhenPageNumberIsInvalid() {
        // given -- condition or setup
        int invalidPageNumber = 0;
        int pageSize = 10;
        // verify
        assertThatThrownBy(() -> {
            userService.getPaginatedUser(invalidPageNumber, pageSize);
        }).isInstanceOf(RuntimeException.class).hasMessage(ValidatorConstants.INVALID_PAGE_SIZE);

        BDDMockito.verify(userRepository, Mockito.never()).findAll(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("getPaginatedUser -(positive) should return paginated data when page number and pagesize are valid")
    public void shouldReturnPaginatedUserList() {
        // given -- condition or setup
        int pageNumber = 1;
        int pageSize = 1;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<User> page = new PageImpl<>(users, pageable, users.size());
        BDDMockito.given(userRepository.findAll(pageable)).willReturn(page);

        // when -- action or behavior that are going to test
        PageResponse<UserResponseDto> pageResponse = userService.getPaginatedUser(pageNumber, pageSize);
        // then -- verify the output

        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.getContent()).isNotEmpty();
        assertThat(pageResponse.getSize()).isEqualTo(pageSize);
        assertThat(pageResponse.getTotalPages()).isEqualTo(3);
        assertThat(pageResponse.getCurrentPageNumber()).isEqualTo(pageNumber);
    }


    @Test
    @DisplayName("getPaginatedUser -(negative) should return empty list when no user found")
    public void returnEmptyListWhenNoUserPresent() {
        // given -- condition or setup
        int pageNumber = 1;
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<User> page = new PageImpl<>(List.of(), pageable, 0);

        BDDMockito.given(userRepository.findAll(pageable)).willReturn(page);
        // when -- action or behavior that are going to test

        PageResponse<UserResponseDto> pageResponse = userService.getPaginatedUser(pageNumber, pageSize);

        // then -- verify the output
        assertThat(pageResponse.getContent()).isEmpty();
        assertThat(pageResponse.getTotalElements()).isEqualTo(0);
        assertThat(pageResponse.getTotalPages()).isEqualTo(0);
    }

    @Test
    @DisplayName("save -(positive) create user and return user response dto")
    public void returnUserResponseDtoOnCreateUser() {
        // given -- condition or setup
        UserRequestDto userRequestDto = new UserRequestDto(user.getFirstName(), user.getLastName(), user.getEmail());

        BDDMockito.given(roleService.getRoleByName(role.getName())).willReturn(role);
        BDDMockito.given(passwordEncoder.encode("mindfire")).willReturn("encodedpassword");
        BDDMockito.given(userRepository.save(Mockito.any(User.class))).willReturn(user);

        // when -- action or behavior that are going to test
        UserResponseDto response = userService.create(userRequestDto);
        // then -- verify the output

        BDDMockito.verify(roleService, BDDMockito.times(1)).getRoleByName("USER");
        BDDMockito.verify(passwordEncoder, BDDMockito.times(1)).encode("mindfire");
        BDDMockito.verify(userRepository, BDDMockito.times(1)).save(Mockito.any(User.class));

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(userRequestDto.email());
        assertThat(response.firstName()).isEqualTo(userRequestDto.firstName());
        assertThat(response.lastName()).isEqualTo(userRequestDto.lastName());
        assertThat(response.role()).isEqualTo(role.getName());
    }
}
