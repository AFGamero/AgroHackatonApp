package com.agrotrace.agrotrace.modules.users.application.service;

import com.agrotrace.agrotrace.modules.users.application.dto.CreateUserDTO;
import com.agrotrace.agrotrace.modules.users.application.dto.UserResponseDTO;
import com.agrotrace.agrotrace.modules.users.application.mapper.UserMapper;
import com.agrotrace.agrotrace.modules.users.domain.exception.UserNotFoundException;
import com.agrotrace.agrotrace.modules.users.domain.model.User;
import com.agrotrace.agrotrace.modules.users.domain.model.UserStatus;
import com.agrotrace.agrotrace.modules.users.domain.repository.UserRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO createUser(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "El correo ya está registrado");
        }
        
        User user = userMapper.toEntity(dto);
        user.setStatus(UserStatus.PENDING_VERIFICATION);
        user.setEmailVerified(false);
        
        // TODO: Encriptar contraseña cuando se implemente seguridad
        // user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setPasswordHash(dto.password()); // Temporal para desarrollo
        
        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> UserNotFoundException.byId(id));
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> UserNotFoundException.byEmail(email));
        return userMapper.toResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toResponseDTO)
            .toList();
    }

    @Transactional
    public UserResponseDTO updateUserStatus(UUID id, UserStatus newStatus) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> UserNotFoundException.byId(id));
        
        user.setStatus(newStatus);
        User updated = userRepository.save(user);
        return userMapper.toResponseDTO(updated);
    }

    @Transactional
    public UserResponseDTO verifyEmail(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> UserNotFoundException.byId(id));
        
        user.setEmailVerified(true);
        if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
            user.setStatus(UserStatus.ACTIVE);
        }
        
        User updated = userRepository.save(user);
        return userMapper.toResponseDTO(updated);
    }
}
