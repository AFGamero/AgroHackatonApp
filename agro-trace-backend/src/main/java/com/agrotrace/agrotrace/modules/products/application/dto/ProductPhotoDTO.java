package com.agrotrace.agrotrace.modules.products.application.dto;

import java.util.UUID;

public record ProductPhotoDTO(
        UUID id,
        String url,
        String description,
        Boolean isCover,
        Integer sortOrder
) {}
