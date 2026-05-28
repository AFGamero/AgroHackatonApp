package com.agrotrace.agrotrace.modules.exports.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateExportDTO(
        @NotBlank String destinationCountry,
        @NotBlank String status,
        @NotNull BigDecimal quantity,
        @NotBlank String unit,
        String shippingCompany,
        String trackingNumber
) {}
