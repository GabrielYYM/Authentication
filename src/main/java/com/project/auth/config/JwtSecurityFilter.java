package com.project.auth.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);

        if (token != null) {
            try {
                DecodedJWT decodedJWT = authService.getDecodedToken(token);
                String email = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();

                List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                if (role != null && !role.isBlank()) {
                    authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName()) || "jwt_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String paramToken = request.getParameter("token");
        if (paramToken != null && !paramToken.isBlank()) {
            return paramToken;
        }

        return null;
    }

}
