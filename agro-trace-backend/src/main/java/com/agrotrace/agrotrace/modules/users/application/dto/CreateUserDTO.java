package com.agrotrace.agrotrace.modules.users.application.dto;

import com.agrotrace.agrotrace.modules.users.domain.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
    @NotBlank(message = "El nombre completo es obligatorio")
    String fullName,
    
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    String email,
    
    @NotBlank(message = "La contraseña es obligatoria")
    String password,
    
    @NotNull(message = "El rol es obligatorio")
    UserRole role
) {
}
