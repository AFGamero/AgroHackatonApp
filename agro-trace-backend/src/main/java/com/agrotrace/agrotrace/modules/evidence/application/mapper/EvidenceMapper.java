package com.agrotrace.agrotrace.modules.evidence.application.mapper;

import com.agrotrace.agrotrace.modules.evidence.application.dto.CreateEvidenceDTO;
import com.agrotrace.agrotrace.modules.evidence.application.dto.EvidenceResponseDTO;
import com.agrotrace.agrotrace.modules.evidence.domain.model.Evidence;
import org.mapstruct.Mapper;

@Mapper(config = com.agrotrace.agrotrace.config.MapperConfig.class)
public interface EvidenceMapper {
    EvidenceResponseDTO toResponseDTO(Evidence evidence);
}
