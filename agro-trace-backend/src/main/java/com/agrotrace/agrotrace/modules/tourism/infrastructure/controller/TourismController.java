package com.agrotrace.agrotrace.modules.tourism.infrastructure.controller;

import com.agrotrace.agrotrace.modules.tourism.application.dto.*;
import com.agrotrace.agrotrace.modules.tourism.application.service.TourismService;
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
@Tag(name = "Tourism", description = "Experiencias turisticas")
public class TourismController {

    private final TourismService tourismService;

    @PostMapping("/tourism/experiences")
    @Operation(summary = "Crear experiencia turistica")
    public ResponseEntity<ExperienceResponseDTO> create(@RequestParam UUID farmId, @Valid @RequestBody CreateExperienceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tourismService.createExperience(farmId, dto));
    }

    @GetMapping("/tourism/experiences/{id}")
    @Operation(summary = "Obtener experiencia por ID")
    public ResponseEntity<ExperienceResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(tourismService.getById(id));
    }

    @GetMapping("/tourism/experiences/farm/{farmId}")
    @Operation(summary = "Listar experiencias de una finca")
    public ResponseEntity<List<ExperienceResponseDTO>> getByFarm(@PathVariable UUID farmId) {
        return ResponseEntity.ok(tourismService.getByFarm(farmId));
    }

    @GetMapping("/public/experiences")
    @Operation(summary = "Catalogo publico de experiencias")
    public ResponseEntity<List<ExperienceResponseDTO>> getPublic() {
        return ResponseEntity.ok(tourismService.getPublicExperiences());
    }

    @PutMapping("/tourism/experiences/{id}")
    @Operation(summary = "Actualizar experiencia")
    public ResponseEntity<ExperienceResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody CreateExperienceDTO dto) {
        return ResponseEntity.ok(tourismService.updateExperience(id, dto));
    }

    @PatchMapping("/tourism/experiences/{id}/publish")
    @Operation(summary = "Publicar experiencia")
    public ResponseEntity<ExperienceResponseDTO> publish(@PathVariable UUID id) {
        return ResponseEntity.ok(tourismService.publishExperience(id));
    }

    @DeleteMapping("/tourism/experiences/{id}")
    @Operation(summary = "Eliminar experiencia")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tourismService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
