package com.clinica.recall.controller;

import com.clinica.recall.dto.request.CompletarAgendamentoRequest;
import com.clinica.recall.dto.response.AgendamentoResponse;
import com.clinica.recall.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @GetMapping("/aguardando-dados")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<List<AgendamentoResponse>> listarAguardandoDados() {
        return ResponseEntity.ok(agendamentoService.listarAguardandoDados());
    }

    @GetMapping("/aguardando-realizacao")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<List<AgendamentoResponse>> listarAguardandoRealizacao() {
        return ResponseEntity.ok(agendamentoService.listarAguardandoRealizacao());
    }

    @PatchMapping("/{id}/completar")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<AgendamentoResponse> completar(
            @PathVariable Long id,
            @Valid @RequestBody CompletarAgendamentoRequest request) {
        return ResponseEntity.ok(agendamentoService.completar(id, request.getProcedimentoId(), request.getDataAgendada()));
    }
}