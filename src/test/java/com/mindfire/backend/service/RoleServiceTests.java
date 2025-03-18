package com.mindfire.backend.service;

import com.mindfire.backend.entity.Role;
import com.mindfire.backend.exception.RoleNotFoundException;
import com.mindfire.backend.repository.RoleRepository;
import com.mindfire.backend.service.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test class for role service")
public class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    @BeforeEach
    void setup() {
        role = Role.builder().
                id(1L).name("ADMIN").
                build();
    }

    @Test
    @DisplayName("find by name throws exception test")
    public void throwExceptionWhenRoleNameNotFound() {

        // given -- condition or setup
        BDDMockito.given(roleRepository.findByName("Hello")).willReturn(Optional.empty());

        // when -- action or behavior that are going to test
        assertThatThrownBy(() -> {
            roleService.getRoleByName("Hello");
        }).isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    @DisplayName("find by role return role when role found")
    public void returnRoleWhenRoleNameFound() {
        // given -- condition or setup
        BDDMockito.given(roleRepository.findByName(role.getName())).willReturn(Optional.of(role));

        // when -- action or behavior that are going to test
        Role result = roleService.getRoleByName("ADMIN");

        // then -- verify the output
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(role.getId());
        assertThat(result.getName()).isEqualTo(role.getName());
    }


}
