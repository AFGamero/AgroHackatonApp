package com.agrotrace.agrotrace.modules.producers.infrastructure.controller;

import com.agrotrace.agrotrace.modules.producers.application.dto.CreateProducerDTO;
import com.agrotrace.agrotrace.modules.producers.application.dto.ProducerResponseDTO;
import com.agrotrace.agrotrace.modules.producers.application.service.ProducerService;
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
@RequestMapping("/producers")
@RequiredArgsConstructor
@Tag(name = "Producers", description = "Gestion de productores")
public class ProducerController {

    private final ProducerService producerService;

    @PostMapping
    @Operation(summary = "Crear perfil de productor")
    public ResponseEntity<ProducerResponseDTO> createProducer(
            @RequestParam UUID userId,
            @Valid @RequestBody CreateProducerDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(producerService.createProducer(userId, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener productor por ID")
    public ResponseEntity<ProducerResponseDTO> getProducerById(@PathVariable UUID id) {
        return ResponseEntity.ok(producerService.getProducerById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener productor por ID de usuario")
    public ResponseEntity<ProducerResponseDTO> getProducerByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(producerService.getProducerByUserId(userId));
    }

    @GetMapping
    @Operation(summary = "Listar todos los productores")
    public ResponseEntity<List<ProducerResponseDTO>> getAllProducers() {
        return ResponseEntity.ok(producerService.getAllProducers());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar perfil de productor")
    public ResponseEntity<ProducerResponseDTO> updateProducer(
            @PathVariable UUID id,
            @Valid @RequestBody CreateProducerDTO dto) {
        return ResponseEntity.ok(producerService.updateProducer(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar productor")
    public ResponseEntity<Void> deactivateProducer(@PathVariable UUID id) {
        producerService.deactivateProducer(id);
        return ResponseEntity.noContent().build();
    }
}
