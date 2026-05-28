package com.agrotrace.agrotrace.modules.evidence.infrastructure.controller;

import com.agrotrace.agrotrace.modules.evidence.application.dto.CreateEvidenceDTO;
import com.agrotrace.agrotrace.modules.evidence.application.dto.EvidenceResponseDTO;
import com.agrotrace.agrotrace.modules.evidence.application.service.EvidenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lots/{lotId}/evidence")
@RequiredArgsConstructor
@Tag(name = "Evidence", description = "Evidencias de lotes")
public class EvidenceController {

    private final EvidenceService evidenceService;

    @PostMapping
    @Operation(summary = "Agregar evidencia a un lote")
    public ResponseEntity<EvidenceResponseDTO> addEvidence(
            @PathVariable UUID lotId,
            @RequestParam UUID userId,
            @Valid @RequestBody CreateEvidenceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evidenceService.addEvidence(userId, lotId, dto));
    }

    @GetMapping
    @Operation(summary = "Listar evidencias de un lote")
    public ResponseEntity<List<EvidenceResponseDTO>> getEvidenceByLot(@PathVariable UUID lotId) {
        return ResponseEntity.ok(evidenceService.getEvidenceByLot(lotId));
    }
}
