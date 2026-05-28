package com.agrotrace.agrotrace.modules.lots.application.dto;

import com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LotResponseDTO(
        UUID id,
        UUID farmId,
        String code,
        String name,
        BigDecimal areaHectares,
        String crop,
        String variety,
        LocalDate plantingDate,
        CertificationStatus currentStatus,
        String description,
        String status
) {}
