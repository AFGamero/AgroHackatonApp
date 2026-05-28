package com.agrotrace.agrotrace.modules.evidence.application.service;

import com.agrotrace.agrotrace.modules.evidence.application.dto.CreateEvidenceDTO;
import com.agrotrace.agrotrace.modules.evidence.application.dto.EvidenceResponseDTO;
import com.agrotrace.agrotrace.modules.evidence.application.mapper.EvidenceMapper;
import com.agrotrace.agrotrace.modules.evidence.domain.model.Evidence;
import com.agrotrace.agrotrace.modules.evidence.domain.repository.EvidenceRepository;
import com.agrotrace.agrotrace.modules.lots.domain.repository.LotRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final EvidenceMapper evidenceMapper;
    private final LotRepository lotRepository;

    @Transactional
    public EvidenceResponseDTO addEvidence(UUID userId, UUID lotId, CreateEvidenceDTO dto) {
        if (!lotRepository.existsById(lotId)) {
            throw new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404);
        }

        Evidence evidence = new Evidence();
        evidence.setLotId(lotId);
        evidence.setType(dto.type());
        evidence.setUrl(dto.url());
        evidence.setComment(dto.comment());
        evidence.setDescription(dto.description());
        evidence.setVisiblePublicly(dto.visiblePublicly());
        evidence.setCertificationStatusEventId(dto.certificationStatusEventId());
        evidence.setRegisteredBy(userId);

        return evidenceMapper.toResponseDTO(evidenceRepository.save(evidence));
    }

    public List<EvidenceResponseDTO> getEvidenceByLot(UUID lotId) {
        return evidenceRepository.findByLotId(lotId).stream()
                .map(evidenceMapper::toResponseDTO)
                .toList();
    }
}
