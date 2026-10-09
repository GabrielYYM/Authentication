package com.project.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerController {

    @GetMapping("/dashboard")
    public Map<String, String> getDashboard() {
        return Map.of(
                "perfil", "CUSTOMER",
                "mensagem", "Acesso autorizado à Tela do Cliente"
        );
    }
}
