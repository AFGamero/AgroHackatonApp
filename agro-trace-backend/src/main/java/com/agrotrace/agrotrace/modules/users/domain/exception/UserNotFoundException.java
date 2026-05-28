package com.agrotrace.agrotrace.modules.users.domain.exception;

import com.agrotrace.agrotrace.shared.exceptions.BusinessException;

import java.util.UUID;

public class UserNotFoundException extends BusinessException {
    
    public UserNotFoundException(String message) {
        super("USER_NOT_FOUND", message, 404);
    }
    
    public static UserNotFoundException byId(UUID id) {
        return new UserNotFoundException("Usuario no encontrado con ID: " + id);
    }
    
    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("Usuario no encontrado con email: " + email);
    }
}
