package com.agrotrace.agrotrace.modules.auth.application.service;

import com.agrotrace.agrotrace.config.JwtConfig;
import com.agrotrace.agrotrace.config.JwtTokenProvider;
import com.agrotrace.agrotrace.modules.auth.application.dto.*;
import com.agrotrace.agrotrace.modules.auth.domain.model.Session;
import com.agrotrace.agrotrace.modules.auth.domain.repository.SessionRepository;
import com.agrotrace.agrotrace.modules.users.domain.model.User;
import com.agrotrace.agrotrace.modules.users.domain.model.UserRole;
import com.agrotrace.agrotrace.modules.users.domain.model.UserStatus;
import com.agrotrace.agrotrace.modules.users.domain.repository.UserRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;

    public TokenResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessException("EMAIL_EXISTS", "El correo ya esta registrado");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setFullName(dto.fullName());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setRole(UserRole.valueOf(dto.role()));
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(false);

        user = userRepository.save(user);
        return generateTokens(user);
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BusinessException("INVALID_CREDENTIALS", "Credenciales invalidas", 401));

        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash())) {
            throw new BusinessException("INVALID_CREDENTIALS", "Credenciales invalidas", 401);
        }

        if (user.getStatus() == UserStatus.INACTIVE || user.getStatus() == UserStatus.BLOCKED) {
            throw new BusinessException("USER_INACTIVE", "Usuario inactivo o bloqueado", 403);
        }

        return generateTokens(user);
    }

    public TokenResponseDTO refresh(RefreshTokenRequestDTO dto) {
        UUID userId = jwtTokenProvider.getUserIdFromToken(dto.refreshToken());

        var activeSessions = sessionRepository.findByUserIdAndStatus(userId, "ACTIVE");
        boolean valid = activeSessions.stream()
                .anyMatch(s -> s.getRefreshTokenHash().equals(
                        String.valueOf(dto.refreshToken().hashCode())));

        if (!valid) {
            throw new BusinessException("INVALID_REFRESH", "Refresh token invalido", 401);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "Usuario no encontrado", 404));

        return generateTokens(user);
    }

    @Transactional
    public void logout(UUID userId, String refreshToken) {
        var activeSessions = sessionRepository.findByUserIdAndStatus(userId, "ACTIVE");
        activeSessions.forEach(s -> {
            s.setStatus("REVOKED");
            s.setRevokedAt(LocalDateTime.now());
            s.setRevocationReason("LOGOUT");
        });
        sessionRepository.saveAll(activeSessions);
    }

    private TokenResponseDTO generateTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        Session session = Session.builder()
                .userId(user.getId())
                .refreshTokenHash(String.valueOf(refreshToken.hashCode()))
                .status("ACTIVE")
                .expiresAt(LocalDateTime.now().plusSeconds(jwtConfig.getRefreshExpiration() / 1000))
                .build();
        sessionRepository.save(session);

        return new TokenResponseDTO(accessToken, refreshToken, jwtConfig.getExpiration() / 1000);
    }
}
