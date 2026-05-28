package com.agrotrace.agrotrace.modules.certifications.application.service;

import com.agrotrace.agrotrace.modules.certifications.application.dto.*;
import com.agrotrace.agrotrace.modules.certifications.domain.model.Certification;
import com.agrotrace.agrotrace.modules.certifications.domain.repository.CertificationRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificationService {

    private final CertificationRepository repository;

    @Transactional
    public CertificationResponseDTO create(UUID farmId, UUID lotId, CreateCertificationDTO dto) {
        Certification cert = new Certification();
        cert.setFarmId(farmId);
        cert.setLotId(lotId);
        cert.setType(dto.type());
        cert.setCertifyingEntity(dto.certifyingEntity());
        cert.setCertificationNumber(dto.certificationNumber());
        cert.setIssueDate(dto.issueDate());
        cert.setExpirationDate(dto.expirationDate());
        cert.setScope(dto.scope());
        cert.setDocumentUrl(dto.documentUrl());
        cert.setStatus(dto.status() != null ? dto.status() : "PENDING");

        return toDTO(repository.save(cert));
    }

    public CertificationResponseDTO getById(UUID id) {
        return toDTO(repository.findById(id)
                .orElseThrow(() -> new BusinessException("CERT_NOT_FOUND", "Certificacion no encontrada", 404)));
    }

    public List<CertificationResponseDTO> getByFarm(UUID farmId) {
        return repository.findAll().stream()
                .filter(c -> farmId.equals(c.getFarmId()))
                .map(this::toDTO).toList();
    }

    public List<CertificationResponseDTO> getByLot(UUID lotId) {
        return repository.findAll().stream()
                .filter(c -> lotId.equals(c.getLotId()))
                .map(this::toDTO).toList();
    }

    public List<CertificationResponseDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public CertificationResponseDTO update(UUID id, CreateCertificationDTO dto) {
        Certification cert = repository.findById(id)
                .orElseThrow(() -> new BusinessException("CERT_NOT_FOUND", "Certificacion no encontrada", 404));

        if (dto.type() != null) cert.setType(dto.type());
        if (dto.certifyingEntity() != null) cert.setCertifyingEntity(dto.certifyingEntity());
        if (dto.certificationNumber() != null) cert.setCertificationNumber(dto.certificationNumber());
        if (dto.issueDate() != null) cert.setIssueDate(dto.issueDate());
        if (dto.expirationDate() != null) cert.setExpirationDate(dto.expirationDate());
        if (dto.scope() != null) cert.setScope(dto.scope());
        if (dto.documentUrl() != null) cert.setDocumentUrl(dto.documentUrl());
        if (dto.status() != null) cert.setStatus(dto.status());

        return toDTO(repository.save(cert));
    }

    private CertificationResponseDTO toDTO(Certification c) {
        return new CertificationResponseDTO(c.getId(), c.getFarmId(), c.getLotId(), c.getType(),
                c.getCertifyingEntity(), c.getCertificationNumber(), c.getIssueDate(),
                c.getExpirationDate(), c.getScope(), c.getDocumentUrl(), c.getStatus());
    }
}
