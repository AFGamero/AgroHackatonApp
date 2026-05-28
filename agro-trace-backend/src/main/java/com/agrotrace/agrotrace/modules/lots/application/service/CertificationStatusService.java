package com.agrotrace.agrotrace.modules.lots.application.service;

import com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatus;
import com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatusEvent;
import com.agrotrace.agrotrace.modules.lots.domain.model.Lot;
import com.agrotrace.agrotrace.modules.lots.domain.repository.LotRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationStatusService {

    private final EntityManager entityManager;
    private final LotRepository lotRepository;

    @Transactional
    public CertificationStatusEvent registerStatus(UUID lotId, UUID registeredBy, CertificationStatus status,
                                                    String observations, UUID certificationId) {
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404));

        CertificationStatusEvent event = CertificationStatusEvent.builder()
                .lotId(lotId)
                .certificationStatus(status)
                .eventDate(java.time.LocalDate.now())
                .observations(observations)
                .registeredBy(registeredBy)
                .build();

        if (certificationId != null) {
            event.setCertification(entityManager.find(
                    com.agrotrace.agrotrace.modules.certifications.domain.model.Certification.class,
                    certificationId));
        }

        entityManager.persist(event);
        lot.setCurrentStatus(status);
        lotRepository.save(lot);

        return event;
    }

    public List<CertificationStatusEvent> getHistory(UUID lotId) {
        return entityManager.createQuery(
                "SELECT e FROM CertificationStatusEvent e WHERE e.lotId = :lotId ORDER BY e.createdAt DESC",
                CertificationStatusEvent.class)
                .setParameter("lotId", lotId)
                .getResultList();
    }
}
