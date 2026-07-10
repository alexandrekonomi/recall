package com.clinica.recall.controller;

import com.clinica.recall.dto.request.PacienteRequest;
import com.clinica.recall.dto.response.PacienteResponse;
import com.clinica.recall.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<List<PacienteResponse>> listar() {
        return ResponseEntity.ok(pacienteService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<PacienteResponse> atualizar(@PathVariable UUID id,
                                                      @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.ok(pacienteService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        pacienteService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}