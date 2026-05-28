package com.agrotrace.agrotrace.modules.purchaserequests.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PurchaseRequestResponseDTO(
        UUID id,
        UUID lotId,
        String country,
        BigDecimal quantity,
        String unit,
        String contactName,
        String company,
        String email,
        String phone,
        String message,
        String status,
        LocalDateTime createdAt
) {}
