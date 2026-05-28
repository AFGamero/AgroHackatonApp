package com.agrotrace.agrotrace.modules.farms.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FarmResponseDTO(
        UUID id,
        UUID producerId,
        String name,
        String location,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal areaHectares,
        String description,
        String status
) {}
