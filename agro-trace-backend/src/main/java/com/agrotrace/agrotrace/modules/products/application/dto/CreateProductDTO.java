package com.agrotrace.agrotrace.modules.products.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateProductDTO(
        @NotNull UUID lotId,
        @NotBlank String name,
        String description,
        @NotBlank String crop,
        String variety,
        @NotNull @Positive BigDecimal quantityAvailable,
        @NotBlank String unit,
        BigDecimal referencePrice,
        String currency,
        LocalDate availabilityDate
) {}
