package com.agrotrace.agrotrace.modules.lots.infrastructure.controller;

import com.agrotrace.agrotrace.modules.lots.application.service.CertificationStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/public/lots/{lotId}/certification-status")
@RequiredArgsConstructor
@Tag(name = "Public Traceability", description = "Trazabilidad publica de certificaciones")
public class PublicCertificationStatusController {

    private final CertificationStatusService service;

    @GetMapping
    @Operation(summary = "Trazabilidad publica de certificaciones")
    public ResponseEntity<List<Map<String, Object>>> getHistory(@PathVariable UUID lotId) {
        var events = service.getHistory(lotId);
        var result = events.stream().map(e -> Map.of(
                "status", (Object) e.getCertificationStatus().name(),
                "date", e.getEventDate().toString(),
                "observations", e.getObservations() != null ? e.getObservations() : ""
        )).toList();
        return ResponseEntity.ok(result);
    }
}
