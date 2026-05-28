package com.agrotrace.agrotrace.modules.passports.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PassportResponseDTO(
        UUID id,
        UUID lotId,
        String publicIdentifier,
        String publicUrl,
        Boolean isPublished,
        LocalDateTime publishedAt
) {}
