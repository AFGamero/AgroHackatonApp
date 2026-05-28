package com.agrotrace.agrotrace.modules.certifications.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CertificationResponseDTO(
        UUID id,
        UUID farmId,
        UUID lotId,
        String type,
        String certifyingEntity,
        String certificationNumber,
        LocalDate issueDate,
        LocalDate expirationDate,
        String scope,
        String documentUrl,
        String status
) {}
