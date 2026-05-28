package com.agrotrace.agrotrace.modules.purchaserequests.application.service;

import com.agrotrace.agrotrace.modules.purchaserequests.application.dto.*;
import com.agrotrace.agrotrace.modules.purchaserequests.domain.model.PurchaseRequest;
import com.agrotrace.agrotrace.modules.purchaserequests.domain.repository.PurchaseRequestRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseRequestService {

    private final PurchaseRequestRepository repository;

    @Transactional
    public PurchaseRequestResponseDTO create(UUID lotId, CreatePurchaseRequestDTO dto) {
        PurchaseRequest pr = PurchaseRequest.builder()
                .lotId(lotId)
                .country(dto.country())
                .quantity(dto.quantity())
                .unit(dto.unit())
                .contactName(dto.contactName())
                .company(dto.company())
                .email(dto.email())
                .phone(dto.phone())
                .message(dto.message())
                .build();

        return toDTO(repository.save(pr));
    }

    public List<PurchaseRequestResponseDTO> getByLot(UUID lotId) {
        return repository.findByLotId(lotId).stream().map(this::toDTO).toList();
    }

    public List<PurchaseRequestResponseDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public PurchaseRequestResponseDTO updateStatus(UUID id, String status) {
        PurchaseRequest pr = repository.findById(id)
                .orElseThrow(() -> new BusinessException("REQUEST_NOT_FOUND", "Solicitud no encontrada", 404));

        pr.setStatus(status);
        return toDTO(repository.save(pr));
    }

    private PurchaseRequestResponseDTO toDTO(PurchaseRequest pr) {
        return new PurchaseRequestResponseDTO(pr.getId(), pr.getLotId(), pr.getCountry(), pr.getQuantity(), pr.getUnit(), pr.getContactName(), pr.getCompany(), pr.getEmail(), pr.getPhone(), pr.getMessage(), pr.getStatus(), pr.getCreatedAt());
    }
}
