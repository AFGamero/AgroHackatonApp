package com.agrotrace.agrotrace.modules.users.application.dto;

import com.agrotrace.agrotrace.modules.users.domain.model.UserRole;
import com.agrotrace.agrotrace.modules.users.domain.model.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String fullName,
    String email,
    UserRole role,
    UserStatus status,
    Boolean emailVerified,
    LocalDateTime createdAt
) {
}
