package com.project.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.auth.DTO.AuthDTO;
import com.project.auth.entity.User;
import com.project.auth.entity.enums.Role;
import com.project.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    private final Set<String> tokenGraveyard = ConcurrentHashMap.newKeySet();

    public AuthDTO.Response register(AuthDTO.RegisterRequest dto) {
        if (dto.role() == Role.ADMIN) {
            throw new RuntimeException("Não é permitido cadastrar o perfil de Administrador");
        }

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Ocorreu um erro com seu cadastro");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role() != null ? dto.role() : Role.CUSTOMER);

        userRepository.save(user);

        return new AuthDTO.Response(null, user.getEmail(), user.getRole(), "Usuário registrado com sucesso!");
    }

    public AuthDTO.Response login(AuthDTO.LoginRequest dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = generateToken(user);

        return new AuthDTO.Response(token, user.getEmail(), user.getRole(), "Login realizado com sucesso!");
    }

    public void logout() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BadCredentialsException("Requisição inválida");
        }
        String authHeader = attributes.getRequest().getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            throw new BadCredentialsException("Token não fornecido no cabeçalho Authorization");
        }
        logout(authHeader);
    }

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new BadCredentialsException("Token não fornecido");
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenGraveyard.add(token);
    }

    public boolean isTokenRevoked(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return tokenGraveyard.contains(token);
    }

    public boolean isTokenInGraveyard(String token) {
        return isTokenRevoked(token);
    }

    public DecodedJWT getDecodedToken(String token) {
        if (token == null || token.isBlank()) {
            throw new BadCredentialsException("Token não fornecido");
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (isTokenRevoked(token)) {
            throw new BadCredentialsException("Token revogado");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("Token inválido ou expirado");
        }
    }

    public String validateToken(String token) {
        return getDecodedToken(token).getSubject();
    }

    private String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getEmail())
                .withClaim("role", user.getRole() != null ? user.getRole().name() : null)
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);
    }
}
