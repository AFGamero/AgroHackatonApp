package com.agrotrace.agrotrace.modules.exports.application.service;

import com.agrotrace.agrotrace.modules.exports.application.dto.*;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional(readOnly = true)
public class ExportService {

    private final Map<UUID, Map<String, Object>> exports = new ConcurrentHashMap<>();

    @Transactional
    public ExportResponseDTO create(UUID lotId, CreateExportDTO dto) {
        UUID id = UUID.randomUUID();
        Map<String, Object> exp = new HashMap<>();
        exp.put("id", id);
        exp.put("lotId", lotId);
        exp.put("destinationCountry", dto.destinationCountry());
        exp.put("status", dto.status());
        exp.put("quantity", dto.quantity());
        exp.put("unit", dto.unit());
        exp.put("shippingCompany", dto.shippingCompany());
        exp.put("trackingNumber", dto.trackingNumber());
        exp.put("createdAt", LocalDateTime.now());
        exports.put(id, exp);
        return toDTO(exp);
    }

    public List<ExportResponseDTO> getByLot(UUID lotId) {
        return exports.values().stream()
                .filter(e -> lotId.equals(e.get("lotId")))
                .map(this::toDTO).toList();
    }

    public List<ExportResponseDTO> getAll() {
        return exports.values().stream().map(this::toDTO).toList();
    }

    @Transactional
    public ExportResponseDTO updateStatus(UUID id, String status, String trackingNumber) {
        Map<String, Object> exp = exports.get(id);
        if (exp == null) throw new BusinessException("EXPORT_NOT_FOUND", "Exportacion no encontrada", 404);
        exp.put("status", status);
        if (trackingNumber != null) exp.put("trackingNumber", trackingNumber);
        return toDTO(exp);
    }

    private ExportResponseDTO toDTO(Map<String, Object> e) {
        return new ExportResponseDTO(
                (UUID) e.get("id"), (UUID) e.get("lotId"),
                (String) e.get("destinationCountry"), (String) e.get("status"),
                (java.math.BigDecimal) e.get("quantity"), (String) e.get("unit"),
                (String) e.get("shippingCompany"), (String) e.get("trackingNumber"),
                (LocalDateTime) e.get("createdAt"));
    }
}
