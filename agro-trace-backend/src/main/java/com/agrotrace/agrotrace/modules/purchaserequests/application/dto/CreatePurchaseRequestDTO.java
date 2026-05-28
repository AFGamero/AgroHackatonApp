package com.agrotrace.agrotrace.modules.purchaserequests.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreatePurchaseRequestDTO(
        @NotBlank String country,
        @NotNull @Positive BigDecimal quantity,
        @NotBlank String unit,
        @NotBlank String contactName,
        String company,
        @NotBlank @Email String email,
        String phone,
        String message
) {}
