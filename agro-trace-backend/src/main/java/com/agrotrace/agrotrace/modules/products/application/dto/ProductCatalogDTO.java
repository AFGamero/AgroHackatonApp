package com.agrotrace.agrotrace.modules.products.application.dto;

import com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatus;
import java.math.BigDecimal;

public record ProductCatalogDTO(
        String lotId,
        String lotName,
        String crop,
        String variety,
        BigDecimal areaHectares,
        CertificationStatus certificationStatus,
        String farmName,
        String producerOrg
) {}
