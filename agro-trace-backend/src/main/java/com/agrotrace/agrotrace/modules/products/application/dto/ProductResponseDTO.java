package com.agrotrace.agrotrace.modules.products.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        UUID lotId,
        String publicId,
        String name,
        String description,
        String crop,
        String variety,
        BigDecimal quantityAvailable,
        String unit,
        BigDecimal referencePrice,
        String currency,
        LocalDate availabilityDate,
        String status,
        LocalDateTime publishedAt,
        UUID createdBy,
        LocalDateTime createdAt,
        List<ProductPhotoDTO> photos
) {}
