package com.clinica.recall.controller;

import com.clinica.recall.dto.request.ConfiguracaoClinicaRequest;
import com.clinica.recall.dto.response.ConfiguracaoClinicaResponse;
import com.clinica.recall.service.ConfiguracaoClinicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuracoes")
@RequiredArgsConstructor
public class ConfiguracaoClinicaController {

    private final ConfiguracaoClinicaService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<ConfiguracaoClinicaResponse> buscar() {
        return ResponseEntity.ok(service.buscar());
    }

    @PutMapping
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ConfiguracaoClinicaResponse> atualizar(
            @Valid @RequestBody ConfiguracaoClinicaRequest request) {
        return ResponseEntity.ok(service.atualizar(request));
    }
}