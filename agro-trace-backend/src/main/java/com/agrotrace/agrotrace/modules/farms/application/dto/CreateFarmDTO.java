package com.agrotrace.agrotrace.modules.farms.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateFarmDTO(
        @NotBlank String name,
        String location,
        BigDecimal latitude,
        BigDecimal longitude,
        @NotNull @Positive BigDecimal areaHectares,
        String description
) {}
