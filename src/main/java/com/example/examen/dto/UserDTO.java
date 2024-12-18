package com.example.examen.dto;

import com.example.examen.authorities.UserRole;
import jakarta.validation.constraints.*;

public record UserDTO(

        @NotBlank(message = "Förnamn får inte vara tomt")
        String firstName,

        @NotBlank(message = "Efternamn får inte vara tomt")
        String lastName,

        @Email(message = "Ska vara en email")
        @NotBlank(message = "Email får inte vara tomt")
        String username,

        @NotBlank(message = "Lösenord måste vara med")
        @Size(min = 4, max = 10, message = "Lösenordet måste vara mellan 4 och 10 tecken")
        String password,

        UserRole userRole
) {}
