package com.agrotrace.agrotrace.modules.producers.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProducerDTO(
        @NotBlank String documentType,
        @NotBlank @Size(max = 50) String documentNumber,
        @Size(max = 20) String phone,
        @Size(max = 120) String organization
) {}
