package com.agrotrace.agrotrace.modules.evidence.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateEvidenceDTO(
        @NotBlank String type,
        @NotBlank String url,
        String comment,
        String description,
        @NotNull Boolean visiblePublicly,
        UUID certificationStatusEventId
) {}
