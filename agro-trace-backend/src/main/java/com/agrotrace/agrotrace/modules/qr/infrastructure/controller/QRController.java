package com.agrotrace.agrotrace.modules.qr.infrastructure.controller;

import com.agrotrace.agrotrace.modules.passports.application.service.PassportService;
import com.agrotrace.agrotrace.modules.qr.application.service.QRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
@Tag(name = "QR", description = "Generacion de codigos QR")
public class QRController {

    private final QRService qrService;
    private final PassportService passportService;

    @GetMapping("/{lotId}")
    @Operation(summary = "Generar QR del pasaporte de un lote")
    public ResponseEntity<Map<String, String>> generateQR(@PathVariable UUID lotId) {
        var passport = passportService.getPassportByLot(lotId);
        String qrBase64 = qrService.getQRImageForPassport(passport.publicUrl());
        return ResponseEntity.ok(Map.of("qrImage", qrBase64, "url", passport.publicUrl()));
    }
}
