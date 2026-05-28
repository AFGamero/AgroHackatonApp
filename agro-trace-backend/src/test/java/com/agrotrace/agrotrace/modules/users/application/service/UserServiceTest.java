package com.agrotrace.agrotrace.modules.users.application.service;

import com.agrotrace.agrotrace.modules.users.application.dto.CreateUserDTO;
import com.agrotrace.agrotrace.modules.users.application.dto.UserResponseDTO;
import com.agrotrace.agrotrace.modules.users.application.mapper.UserMapper;
import com.agrotrace.agrotrace.modules.users.domain.model.User;
import com.agrotrace.agrotrace.modules.users.domain.model.UserRole;
import com.agrotrace.agrotrace.modules.users.domain.model.UserStatus;
import com.agrotrace.agrotrace.modules.users.domain.repository.UserRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private CreateUserDTO createUserDTO;
    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        createUserDTO = new CreateUserDTO("Juan Perez", "juan@test.com", "password123", UserRole.PRODUCTOR);

        user = new User();
        user.setId(userId);
        user.setFullName("Juan Perez");
        user.setEmail("juan@test.com");
        user.setRole(UserRole.PRODUCTOR);
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(false);
    }

    @Test
    void createUser_shouldReturnUserResponseDTO() {
        when(userRepository.existsByEmail(createUserDTO.email())).thenReturn(false);
        when(userMapper.toEntity(createUserDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(
                new UserResponseDTO(userId, "Juan Perez", "juan@test.com", UserRole.PRODUCTOR, UserStatus.PENDING_VERIFICATION, false, null));

        UserResponseDTO result = userService.createUser(createUserDTO);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("juan@test.com");
        assertThat(result.role()).isEqualTo(UserRole.PRODUCTOR);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_withExistingEmail_shouldThrowException() {
        when(userRepository.existsByEmail(createUserDTO.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(createUserDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("El correo ya está registrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(
                new UserResponseDTO(userId, "Juan Perez", "juan@test.com", UserRole.PRODUCTOR, UserStatus.ACTIVE, false, null));

        UserResponseDTO result = userService.getUserById(userId);

        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo("Juan Perez");
    }

    @Test
    void getUserById_notFound_shouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void verifyEmail_shouldSetEmailVerified() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(
                new UserResponseDTO(userId, "Juan Perez", "juan@test.com", UserRole.PRODUCTOR, UserStatus.ACTIVE, true, null));

        UserResponseDTO result = userService.verifyEmail(userId);

        assertThat(result.emailVerified()).isTrue();
    }
}
