package com.agrotrace.agrotrace.modules.tourism.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateExperienceDTO(
        @NotBlank String name,
        String description,
        @NotNull @Positive Integer durationMinutes,
        @NotNull @Positive BigDecimal price,
        @NotNull @Positive Integer capacity,
        String availability
) {}
