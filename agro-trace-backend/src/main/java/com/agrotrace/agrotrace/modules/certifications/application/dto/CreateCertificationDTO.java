package com.agrotrace.agrotrace.modules.certifications.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateCertificationDTO(
        @NotBlank String type,
        @NotBlank String certifyingEntity,
        String certificationNumber,
        @NotNull LocalDate issueDate,
        @NotNull LocalDate expirationDate,
        @NotBlank String scope,
        String documentUrl,
        String status
) {}
