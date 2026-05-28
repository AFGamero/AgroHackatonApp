package com.agrotrace.agrotrace.modules.auth.application.dto;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
