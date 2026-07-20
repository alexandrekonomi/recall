package com.clinica.recall.controller;

import com.clinica.recall.domain.entity.Usuario;
import com.clinica.recall.dto.request.LoginRequest;
import com.clinica.recall.dto.response.LoginResponse;
import com.clinica.recall.repository.UsuarioRepository;
import com.clinica.recall.security.JwtService;
import com.clinica.recall.security.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String email = request.getEmail();

        if (loginAttemptService.estaBloqueado(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("message", "Muitas tentativas incorretas. Tente novamente em alguns minutos."));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getSenha())
            );
        } catch (BadCredentialsException e) {
            loginAttemptService.registrarFalha(email);
            throw e;
        }

        loginAttemptService.registrarSucesso(email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        String token = jwtService.gerarToken(userDetails, Map.of("perfil", usuario.getPerfil().name()));

        return ResponseEntity.ok(new LoginResponse(token, usuario.getPerfil().name(), usuario.getNome()));
    }
}