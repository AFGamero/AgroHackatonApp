package com.agrotrace.agrotrace.modules.producers.application.dto;

import java.util.UUID;

public record ProducerResponseDTO(
        UUID id,
        UUID userId,
        String documentType,
        String documentNumber,
        String phone,
        String organization,
        String status
) {}
