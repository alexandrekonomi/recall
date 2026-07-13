package com.clinica.recall.controller;

import com.clinica.recall.dto.request.RegistrarContatoRequest;
import com.clinica.recall.dto.response.ContatoResponse;
import com.clinica.recall.dto.response.ListaDiariaResponse;
import com.clinica.recall.dto.response.MetricasDashboardResponse;
import com.clinica.recall.service.ContatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contatos")
@RequiredArgsConstructor
public class ContatoController {

    private final ContatoService contatoService;

    @GetMapping("/lista-diaria")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<List<ListaDiariaResponse>> listarParaContactarHoje() {
        return ResponseEntity.ok(contatoService.listarParaContactarHoje());
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<ContatoResponse> registrarResultado(
            @Valid @RequestBody RegistrarContatoRequest request) {
        return ResponseEntity.ok(contatoService.registrarResultado(request));
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<List<ContatoResponse>> listarPorPaciente(
            @PathVariable Long
                    pacienteId) {
        return ResponseEntity.ok(contatoService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/metricas-hoje")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<MetricasDashboardResponse> metricasHoje() {
        return ResponseEntity.ok(contatoService.buscarMetricasHoje());
    }
}