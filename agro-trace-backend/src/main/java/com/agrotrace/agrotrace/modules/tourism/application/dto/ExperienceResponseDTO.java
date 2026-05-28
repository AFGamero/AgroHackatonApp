package com.agrotrace.agrotrace.modules.tourism.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ExperienceResponseDTO(
        UUID id,
        UUID farmId,
        String name,
        String description,
        Integer durationMinutes,
        BigDecimal price,
        Integer capacity,
        String availability,
        Boolean isPublished
) {}
