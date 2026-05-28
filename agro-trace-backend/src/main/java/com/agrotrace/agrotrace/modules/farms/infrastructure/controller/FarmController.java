package com.agrotrace.agrotrace.modules.farms.infrastructure.controller;

import com.agrotrace.agrotrace.modules.farms.application.dto.CreateFarmDTO;
import com.agrotrace.agrotrace.modules.farms.application.dto.FarmResponseDTO;
import com.agrotrace.agrotrace.modules.farms.application.service.FarmService;
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
@RequestMapping("/farms")
@RequiredArgsConstructor
@Tag(name = "Farms", description = "Gestion de fincas")
public class FarmController {

    private final FarmService farmService;

    @PostMapping
    @Operation(summary = "Crear una nueva finca")
    public ResponseEntity<FarmResponseDTO> createFarm(
            @RequestParam UUID producerId,
            @Valid @RequestBody CreateFarmDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(farmService.createFarm(producerId, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener finca por ID")
    public ResponseEntity<FarmResponseDTO> getFarmById(@PathVariable UUID id) {
        return ResponseEntity.ok(farmService.getFarmById(id));
    }

    @GetMapping("/producer/{producerId}")
    @Operation(summary = "Listar fincas de un productor")
    public ResponseEntity<List<FarmResponseDTO>> getFarmsByProducer(@PathVariable UUID producerId) {
        return ResponseEntity.ok(farmService.getFarmsByProducer(producerId));
    }

    @GetMapping
    @Operation(summary = "Listar todas las fincas")
    public ResponseEntity<List<FarmResponseDTO>> getAllFarms() {
        return ResponseEntity.ok(farmService.getAllFarms());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una finca")
    public ResponseEntity<FarmResponseDTO> updateFarm(
            @PathVariable UUID id,
            @Valid @RequestBody CreateFarmDTO dto) {
        return ResponseEntity.ok(farmService.updateFarm(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar una finca")
    public ResponseEntity<Void> deactivateFarm(@PathVariable UUID id) {
        farmService.deactivateFarm(id);
        return ResponseEntity.noContent().build();
    }
}
