package com.example.examen.dto;

import com.example.examen.authorities.UserRole;

public record UserDTO(
        String username,
        String password,
        UserRole userRole
) {}
