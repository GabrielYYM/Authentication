package com.project.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandling {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity Erro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity Erro400(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity BadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage() != null ? ex.getMessage() : "Credenciais inválidas");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity AcessoNegado(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado: você não tem permissão para acessar este recurso.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity RegraDeNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    public record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
