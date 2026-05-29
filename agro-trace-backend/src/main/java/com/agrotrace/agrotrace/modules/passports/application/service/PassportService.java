package com.agrotrace.agrotrace.modules.passports.application.service;

import com.agrotrace.agrotrace.modules.evidence.domain.repository.EvidenceRepository;
import com.agrotrace.agrotrace.modules.farms.domain.repository.FarmRepository;
import com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatus;
import com.agrotrace.agrotrace.modules.lots.domain.repository.LotRepository;
import com.agrotrace.agrotrace.modules.passports.application.dto.PassportResponseDTO;
import com.agrotrace.agrotrace.modules.passports.domain.model.DigitalPassport;
import com.agrotrace.agrotrace.modules.passports.domain.repository.DigitalPassportRepository;
import com.agrotrace.agrotrace.modules.producers.domain.repository.ProducerRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassportService {

    private final DigitalPassportRepository passportRepository;
    private final LotRepository lotRepository;
    private final FarmRepository farmRepository;
    private final ProducerRepository producerRepository;
    private final EvidenceRepository evidenceRepository;
    private final EntityManager entityManager;

    @Transactional
    public PassportResponseDTO generatePassport(UUID lotId, String baseUrl) {
        if (!lotRepository.existsById(lotId)) {
            throw new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404);
        }

        passportRepository.findByLotId(lotId).ifPresent(p -> {
            throw new BusinessException("PASSPORT_EXISTS", "El lote ya tiene un pasaporte digital");
        });

        String publicIdentifier = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String publicUrl = baseUrl + "/public/passports/" + publicIdentifier;

        DigitalPassport passport = DigitalPassport.builder()
                .lotId(lotId)
                .publicIdentifier(publicIdentifier)
                .publicUrl(publicUrl)
                .build();

        passport = passportRepository.save(passport);
        return toDTO(passport);
    }

    public PassportResponseDTO getPassportByLot(UUID lotId) {
        return passportRepository.findByLotId(lotId)
                .map(this::toDTO)
                .orElseThrow(() -> new BusinessException("PASSPORT_NOT_FOUND", "Pasaporte no encontrado", 404));
    }

    public Map<String, Object> getPublicPassport(String publicIdentifier) {
        DigitalPassport passport = passportRepository.findByPublicIdentifier(publicIdentifier)
                .orElseThrow(() -> new BusinessException("PASSPORT_NOT_FOUND", "Pasaporte no encontrado", 404));

        var lot = lotRepository.findById(passport.getLotId())
                .orElseThrow(() -> new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404));

        var farm = farmRepository.findById(lot.getFarmId()).orElse(null);
        var producer = farm != null ? producerRepository.findById(farm.getProducerId()).orElse(null) : null;

        var evidence = evidenceRepository.findByLotId(lot.getId()).stream()
                .filter(e -> Boolean.TRUE.equals(e.getVisiblePublicly()))
                .map(e -> Map.of("type", e.getType(), "url", e.getUrl(), "description", e.getDescription() != null ? e.getDescription() : ""))
                .toList();

        var certHistory = entityManager.createQuery(
                "SELECT e FROM CertificationStatusEvent e WHERE e.lotId = :lotId ORDER BY e.createdAt DESC",
                com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatusEvent.class)
                .setParameter("lotId", lot.getId())
                .getResultList().stream()
                .map(e -> Map.of("status", e.getCertificationStatus().name(), "date", e.getEventDate().toString(), "observations", e.getObservations() != null ? e.getObservations() : ""))
                .toList();

        return Map.of(
                "passportId", passport.getPublicIdentifier(),
                "lot", Map.of(
                        "name", lot.getName(),
                        "code", lot.getCode(),
                        "crop", lot.getCrop(),
                        "variety", lot.getVariety() != null ? lot.getVariety() : "",
                        "areaHectares", lot.getAreaHectares().toString(),
                        "plantingDate", lot.getPlantingDate().toString(),
                        "description", lot.getDescription() != null ? lot.getDescription() : ""
                ),
                "farm", Map.of(
                        "name", farm != null ? farm.getName() : "",
                        "location", farm != null && farm.getLocation() != null ? farm.getLocation() : ""
                ),
                "producer", Map.of(
                        "organization", producer != null && producer.getOrganization() != null ? producer.getOrganization() : ""
                ),
                "certificationStatus", lot.getCurrentStatus().name(),
                "certificationHistory", certHistory,
                "evidence", evidence
        );
    }

    @Transactional
    public PassportResponseDTO publishPassport(UUID lotId) {
        DigitalPassport passport = passportRepository.findByLotId(lotId)
                .orElseThrow(() -> new BusinessException("PASSPORT_NOT_FOUND", "Pasaporte no encontrado", 404));

        passport.setIsPublished(true);
        passport.setPublishedAt(LocalDateTime.now());
        passport = passportRepository.save(passport);
        return toDTO(passport);
    }

    private PassportResponseDTO toDTO(DigitalPassport p) {
        return new PassportResponseDTO(p.getId(), p.getLotId(), p.getPublicIdentifier(), p.getPublicUrl(), p.getIsPublished(), p.getPublishedAt());
    }
}
