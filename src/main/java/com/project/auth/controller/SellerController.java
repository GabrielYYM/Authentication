package com.project.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/seller")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('SELLER')")
public class SellerController {

    @GetMapping("/dashboard")
    public Map<String, String> getDashboard() {
        return Map.of(
                "perfil", "SELLER",
                "mensagem", "Acesso autorizado à Tela do Vendedor"
        );
    }
}
