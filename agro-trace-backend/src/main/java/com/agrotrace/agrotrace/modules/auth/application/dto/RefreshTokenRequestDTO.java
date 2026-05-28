package com.agrotrace.agrotrace.modules.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank String refreshToken
) {}
