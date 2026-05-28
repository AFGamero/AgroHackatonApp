package com.agrotrace.agrotrace.modules.certifications.infrastructure.controller;

import com.agrotrace.agrotrace.modules.certifications.application.dto.*;
import com.agrotrace.agrotrace.modules.certifications.application.service.CertificationService;
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
@RequestMapping("/certifications")
@RequiredArgsConstructor
@Tag(name = "Certifications", description = "Gestion de certificaciones")
public class CertificationController {

    private final CertificationService service;

    @PostMapping
    @Operation(summary = "Registrar certificacion")
    public ResponseEntity<CertificationResponseDTO> create(
            @RequestParam UUID farmId,
            @RequestParam(required = false) UUID lotId,
            @Valid @RequestBody CreateCertificationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(farmId, lotId, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener certificacion por ID")
    public ResponseEntity<CertificationResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/farm/{farmId}")
    @Operation(summary = "Listar certificaciones de una finca")
    public ResponseEntity<List<CertificationResponseDTO>> getByFarm(@PathVariable UUID farmId) {
        return ResponseEntity.ok(service.getByFarm(farmId));
    }

    @GetMapping("/lot/{lotId}")
    @Operation(summary = "Listar certificaciones de un lote")
    public ResponseEntity<List<CertificationResponseDTO>> getByLot(@PathVariable UUID lotId) {
        return ResponseEntity.ok(service.getByLot(lotId));
    }

    @GetMapping
    @Operation(summary = "Listar todas las certificaciones")
    public ResponseEntity<List<CertificationResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar certificacion")
    public ResponseEntity<CertificationResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateCertificationDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }
}
