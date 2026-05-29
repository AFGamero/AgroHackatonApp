package com.agrotrace.agrotrace.modules.products.application.dto;

import com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatus;
import java.math.BigDecimal;
import java.util.List;

public record PublicProductDTO(
        String id,
        String name,
        String description,
        String crop,
        String variety,
        BigDecimal quantityAvailable,
        String unit,
        BigDecimal referencePrice,
        String currency,
        CertificationStatus certification,
        String coverImage,
        List<String> images,
        String farmName,
        String location,
        String passportUrl
) {}
