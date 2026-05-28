package com.agrotrace.agrotrace.modules.lots.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateLotDTO(
        @NotBlank String code,
        @NotBlank String name,
        @NotNull @Positive BigDecimal areaHectares,
        @NotBlank String crop,
        String variety,
        @NotNull LocalDate plantingDate,
        String description
) {}
