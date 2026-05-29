package com.agrotrace.agrotrace.modules.passports.infrastructure.controller;

import com.agrotrace.agrotrace.modules.passports.application.dto.PassportResponseDTO;
import com.agrotrace.agrotrace.modules.passports.application.service.PassportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Passports", description = "Pasaporte digital de lotes")
public class PassportController {

    private final PassportService passportService;

    @PostMapping("/passports")
    @Operation(summary = "Generar pasaporte digital para un lote")
    public ResponseEntity<PassportResponseDTO> generatePassport(
            @RequestParam UUID lotId,
            @RequestParam(defaultValue = "http://localhost:8080/api/v1") String baseUrl) {
        return ResponseEntity.ok(passportService.generatePassport(lotId, baseUrl));
    }

    @GetMapping("/passports/{lotId}")
    @Operation(summary = "Obtener pasaporte por ID de lote")
    public ResponseEntity<PassportResponseDTO> getPassportByLot(@PathVariable UUID lotId) {
        return ResponseEntity.ok(passportService.getPassportByLot(lotId));
    }

    @GetMapping("/public/passports/{publicIdentifier}")
    @Operation(summary = "Obtener pasaporte publico con toda la trazabilidad")
    public ResponseEntity<Map<String, Object>> getPublicPassport(@PathVariable String publicIdentifier) {
        return ResponseEntity.ok(passportService.getPublicPassport(publicIdentifier));
    }

    @PatchMapping("/passports/{lotId}/publish")
    @Operation(summary = "Publicar pasaporte digital")
    public ResponseEntity<PassportResponseDTO> publishPassport(@PathVariable UUID lotId) {
        return ResponseEntity.ok(passportService.publishPassport(lotId));
    }
}
