package com.project.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class WebViewController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private void addCommonAttributes(Model model, String targetPage, String title) {
        model.addAttribute("appName", "Investir Mais - Auth & Dashboard");
        model.addAttribute("pageTitle", title);
        model.addAttribute("targetPage", targetPage);
        model.addAttribute("serverTime", LocalDateTime.now().format(FORMATTER));
        model.addAttribute("serverEngine", "Thymeleaf 3 + Spring Boot");
        model.addAttribute("activeProfile", "dev");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("authenticatedUser", auth.getName());
            model.addAttribute("authorities", auth.getAuthorities().toString());
        } else {
            model.addAttribute("authenticatedUser", null);
            model.addAttribute("authorities", "ANONYMOUS");
        }
    }

    @GetMapping({"/", "/view/login"})
    public String index(Model model) {
        addCommonAttributes(model, "Login", "Login | Thymeleaf + React");
        return "index";
    }

    @GetMapping({"/admin", "/view/admin", "/admin-view"})
    public String adminView(Model model) {
        addCommonAttributes(model, "AdminPage", "Painel Administrativo | Thymeleaf + React");
        model.addAttribute("roleRequired", "ADMIN");
        model.addAttribute("serverMessage", "Template admin.html renderizado no servidor via Thymeleaf.");
        return "admin";
    }

    @GetMapping({"/customer", "/view/customer"})
    public String customerView(Model model) {
        addCommonAttributes(model, "CustomerPage", "Painel do Cliente | Thymeleaf + React");
        model.addAttribute("roleRequired", "CUSTOMER");
        model.addAttribute("serverMessage", "Template renderizado no servidor via Thymeleaf.");
        return "index";
    }

    @GetMapping({"/seller", "/view/seller"})
    public String sellerView(Model model) {
        addCommonAttributes(model, "SellerPage", "Painel do Vendedor | Thymeleaf + React");
        model.addAttribute("roleRequired", "SELLER");
        model.addAttribute("serverMessage", "Template renderizado no servidor via Thymeleaf.");
        return "index";
    }
}
