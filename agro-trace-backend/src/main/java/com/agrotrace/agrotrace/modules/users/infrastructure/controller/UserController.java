package com.agrotrace.agrotrace.modules.users.infrastructure.controller;

import com.agrotrace.agrotrace.modules.users.application.dto.CreateUserDTO;
import com.agrotrace.agrotrace.modules.users.application.dto.UserResponseDTO;
import com.agrotrace.agrotrace.modules.users.application.service.UserService;
import com.agrotrace.agrotrace.modules.users.domain.model.UserStatus;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gestión de usuarios y autenticación")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        UserResponseDTO response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener usuario por email")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado del usuario")
    public ResponseEntity<UserResponseDTO> updateUserStatus(
            @PathVariable UUID id,
            @RequestParam UserStatus status) {
        return ResponseEntity.ok(userService.updateUserStatus(id, status));
    }

    @PatchMapping("/{id}/verify-email")
    @Operation(summary = "Verificar correo del usuario")
    public ResponseEntity<UserResponseDTO> verifyEmail(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.verifyEmail(id));
    }
}
