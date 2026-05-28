package com.agrotrace.agrotrace.modules.exports.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExportResponseDTO(
        UUID id,
        UUID lotId,
        String destinationCountry,
        String status,
        BigDecimal quantity,
        String unit,
        String shippingCompany,
        String trackingNumber,
        LocalDateTime createdAt
) {}
