package com.project.auth.DTO;

import com.project.auth.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDTO {

    public record RegisterRequest(
            @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,

            @NotBlank(message = "A senha é obrigatória") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String password,

            Role role) {
    }

    public record LoginRequest(
            @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,

            @NotBlank(message = "A senha é obrigatória") String password) {
    }

    public record Response(
            String token,
            String email,
            Role role,
            String message) {
    }
}
