package com.agrotrace.agrotrace.modules.evidence.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EvidenceResponseDTO(
        UUID id,
        UUID lotId,
        UUID certificationStatusEventId,
        String type,
        String url,
        String comment,
        String description,
        Boolean visiblePublicly,
        UUID registeredBy,
        LocalDateTime createdAt
) {}
