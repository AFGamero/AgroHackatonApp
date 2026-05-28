package com.agrotrace.agrotrace.modules.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String role
) {}
