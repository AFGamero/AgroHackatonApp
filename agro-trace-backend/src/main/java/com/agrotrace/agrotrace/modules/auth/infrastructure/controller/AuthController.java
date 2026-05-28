package com.agrotrace.agrotrace.modules.auth.infrastructure.controller;

import com.agrotrace.agrotrace.modules.auth.application.dto.*;
import com.agrotrace.agrotrace.modules.auth.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Autenticacion y gestion de sesiones")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<TokenResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar tokens con refresh token")
    public ResponseEntity<TokenResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        return ResponseEntity.ok(authService.refresh(dto));
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesion (revocar refresh tokens)")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok().build();
    }
}
