package com.agrotrace.agrotrace.modules.lots.infrastructure.controller;

import com.agrotrace.agrotrace.modules.lots.application.service.CertificationStatusService;
import com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatus;
import com.agrotrace.agrotrace.modules.lots.domain.model.CertificationStatusEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/lots/{lotId}/certification-status")
@RequiredArgsConstructor
@Tag(name = "Certification Status", description = "Registro de estados de certificacion")
public class CertificationStatusController {

    private final CertificationStatusService service;

    @PostMapping
    @Operation(summary = "Registrar cambio de estado de certificacion")
    public ResponseEntity<CertificationStatusEvent> register(
            @PathVariable UUID lotId,
            @RequestParam UUID registeredBy,
            @RequestParam CertificationStatus status,
            @RequestParam(required = false) String observations,
            @RequestParam(required = false) UUID certificationId) {
        return ResponseEntity.ok(service.registerStatus(lotId, registeredBy, status, observations, certificationId));
    }

    @GetMapping
    @Operation(summary = "Consultar historial de estados de certificacion")
    public ResponseEntity<List<CertificationStatusEvent>> getHistory(@PathVariable UUID lotId) {
        return ResponseEntity.ok(service.getHistory(lotId));
    }

    @GetMapping("/public/{lotId}")
    @Operation(summary = "Trazabilidad publica de certificaciones")
    public ResponseEntity<List<Map<String, Object>>> getPublicHistory(@PathVariable UUID lotId) {
        var events = service.getHistory(lotId);
        var result = events.stream().map(e -> Map.of(
                "status", (Object) e.getCertificationStatus().name(),
                "date", e.getEventDate().toString(),
                "observations", e.getObservations() != null ? e.getObservations() : ""
        )).toList();
        return ResponseEntity.ok(result);
    }
}
