package com.clinica.recall.controller;

import com.clinica.recall.domain.entity.Usuario;
import com.clinica.recall.dto.request.LoginRequest;
import com.clinica.recall.dto.response.LoginResponse;
import com.clinica.recall.repository.UsuarioRepository;
import com.clinica.recall.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {

            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            String senha123 = b.encode("senha123");
            System.out.println(senha123);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );
        } catch (Exception e) {
            System.out.println("=== ERRO AUTH: " + e.getClass().getName() + " - " + e.getMessage());
            throw e;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtService.gerarToken(userDetails, Map.of("perfil", usuario.getPerfil().name()));

        return ResponseEntity.ok(new LoginResponse(token, usuario.getPerfil().name(), usuario.getNome()));
    }

}