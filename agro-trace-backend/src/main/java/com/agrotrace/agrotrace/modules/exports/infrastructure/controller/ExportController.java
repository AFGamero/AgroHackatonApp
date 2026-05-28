package com.agrotrace.agrotrace.modules.exports.infrastructure.controller;

import com.agrotrace.agrotrace.modules.exports.application.dto.*;
import com.agrotrace.agrotrace.modules.exports.application.service.ExportService;
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
@RequestMapping("/exports")
@RequiredArgsConstructor
@Tag(name = "Exports", description = "Gestion de exportaciones")
public class ExportController {

    private final ExportService service;

    @PostMapping
    @Operation(summary = "Registrar exportacion")
    public ResponseEntity<ExportResponseDTO> create(@RequestParam UUID lotId, @Valid @RequestBody CreateExportDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(lotId, dto));
    }

    @GetMapping("/lot/{lotId}")
    @Operation(summary = "Exportaciones de un lote")
    public ResponseEntity<List<ExportResponseDTO>> getByLot(@PathVariable UUID lotId) {
        return ResponseEntity.ok(service.getByLot(lotId));
    }

    @GetMapping
    @Operation(summary = "Todas las exportaciones")
    public ResponseEntity<List<ExportResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado logístico")
    public ResponseEntity<ExportResponseDTO> updateStatus(
            @PathVariable UUID id, @RequestParam String status, @RequestParam(required = false) String trackingNumber) {
        return ResponseEntity.ok(service.updateStatus(id, status, trackingNumber));
    }
}
