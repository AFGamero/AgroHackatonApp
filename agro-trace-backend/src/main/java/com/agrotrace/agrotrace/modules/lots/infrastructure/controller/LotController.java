package com.agrotrace.agrotrace.modules.lots.infrastructure.controller;

import com.agrotrace.agrotrace.modules.lots.application.dto.CreateLotDTO;
import com.agrotrace.agrotrace.modules.lots.application.dto.LotResponseDTO;
import com.agrotrace.agrotrace.modules.lots.application.service.LotService;
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
@RequestMapping("/lots")
@RequiredArgsConstructor
@Tag(name = "Lots", description = "Gestion de lotes")
public class LotController {

    private final LotService lotService;

    @PostMapping
    @Operation(summary = "Crear un nuevo lote")
    public ResponseEntity<LotResponseDTO> createLot(
            @RequestParam UUID farmId,
            @Valid @RequestBody CreateLotDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lotService.createLot(farmId, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener lote por ID")
    public ResponseEntity<LotResponseDTO> getLotById(@PathVariable UUID id) {
        return ResponseEntity.ok(lotService.getLotById(id));
    }

    @GetMapping("/farm/{farmId}")
    @Operation(summary = "Listar lotes de una finca")
    public ResponseEntity<List<LotResponseDTO>> getLotsByFarm(@PathVariable UUID farmId) {
        return ResponseEntity.ok(lotService.getLotsByFarm(farmId));
    }

    @GetMapping
    @Operation(summary = "Listar todos los lotes")
    public ResponseEntity<List<LotResponseDTO>> getAllLots() {
        return ResponseEntity.ok(lotService.getAllLots());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar lote")
    public ResponseEntity<LotResponseDTO> updateLot(
            @PathVariable UUID id,
            @Valid @RequestBody CreateLotDTO dto) {
        return ResponseEntity.ok(lotService.updateLot(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar lote")
    public ResponseEntity<Void> deactivateLot(@PathVariable UUID id) {
        lotService.deactivateLot(id);
        return ResponseEntity.noContent().build();
    }
}
