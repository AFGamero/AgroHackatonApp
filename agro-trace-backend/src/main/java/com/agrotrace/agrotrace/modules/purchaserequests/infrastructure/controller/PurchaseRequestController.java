package com.agrotrace.agrotrace.modules.purchaserequests.infrastructure.controller;

import com.agrotrace.agrotrace.modules.purchaserequests.application.dto.*;
import com.agrotrace.agrotrace.modules.purchaserequests.application.service.PurchaseRequestService;
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
@RequiredArgsConstructor
@Tag(name = "Purchase Requests", description = "Solicitudes de compra internacional")
public class PurchaseRequestController {

    private final PurchaseRequestService service;

    @PostMapping("/public/purchase-requests")
    @Operation(summary = "Crear solicitud de compra (publico)")
    public ResponseEntity<PurchaseRequestResponseDTO> create(
            @RequestParam UUID lotId,
            @Valid @RequestBody CreatePurchaseRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(lotId, dto));
    }

    @GetMapping("/purchase-requests/lot/{lotId}")
    @Operation(summary = "Listar solicitudes por lote")
    public ResponseEntity<List<PurchaseRequestResponseDTO>> getByLot(@PathVariable UUID lotId) {
        return ResponseEntity.ok(service.getByLot(lotId));
    }

    @GetMapping("/purchase-requests")
    @Operation(summary = "Listar todas las solicitudes")
    public ResponseEntity<List<PurchaseRequestResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PatchMapping("/purchase-requests/{id}/status")
    @Operation(summary = "Actualizar estado de solicitud")
    public ResponseEntity<PurchaseRequestResponseDTO> updateStatus(
            @PathVariable UUID id, @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
}
