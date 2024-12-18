package com.example.examen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordDT0(

        @NotBlank(message = "Lösenord måste vara med")
        String currentPassword,

        @NotBlank(message = "Nytt lösenord måste vara med")
        @Size(min = 4, max = 10, message = "Lösenordet måste vara mellan 4 och 10 tecken")
        String newPassword
) {
}
