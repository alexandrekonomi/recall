package com.clinica.recall.controller;

import com.clinica.recall.dto.request.ProcedimentoRequest;
import com.clinica.recall.dto.response.ProcedimentoResponse;
import com.clinica.recall.service.ProcedimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    @PostMapping
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ProcedimentoResponse> criar(@Valid @RequestBody ProcedimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimentoService.criar(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<List<ProcedimentoResponse>> listar() {
        return ResponseEntity.ok(procedimentoService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<ProcedimentoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(procedimentoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ProcedimentoResponse> atualizar(@PathVariable UUID id,
                                                          @Valid @RequestBody ProcedimentoRequest request) {
        return ResponseEntity.ok(procedimentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        procedimentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}