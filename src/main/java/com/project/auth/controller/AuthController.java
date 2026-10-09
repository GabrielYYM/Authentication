package com.project.auth.controller;

import com.project.auth.DTO.AuthDTO;
import com.project.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthDTO.Response register(@Valid @RequestBody AuthDTO.RegisterRequest dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthDTO.Response login(@Valid @RequestBody AuthDTO.LoginRequest dto) {
        return authService.login(dto);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> logout() {
        authService.logout();
        return Map.of("message", "Logout realizado com sucesso!");
    }
}
