package com.clinica.recall.controller;

import com.clinica.recall.dto.request.AtivarContaRequest;
import com.clinica.recall.dto.request.ConvidarUsuarioRequest;
import com.clinica.recall.dto.response.ConviteInfoResponse;
import com.clinica.recall.dto.response.UsuarioResponse;
import com.clinica.recall.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/usuarios/convidar")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<UsuarioResponse> convidar(@Valid @RequestBody ConvidarUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.convidar(request));
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PatchMapping("/usuarios/{id}/desativar")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/usuarios/{id}/reativar")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        usuarioService.reativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/convite/{token}")
    public ResponseEntity<ConviteInfoResponse> buscarConvite(@PathVariable String token) {
        return ResponseEntity.ok(usuarioService.buscarConvite(token));
    }

    @PostMapping("/auth/ativar-conta")
    public ResponseEntity<Void> ativarConta(@Valid @RequestBody AtivarContaRequest request) {
        usuarioService.ativarConta(request);
        return ResponseEntity.ok().build();
    }
}