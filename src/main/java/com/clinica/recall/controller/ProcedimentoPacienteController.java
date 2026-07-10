package com.clinica.recall.controller;

import com.clinica.recall.dto.request.ProcedimentoPacienteRequest;
import com.clinica.recall.dto.response.ProcedimentoPacienteResponse;
import com.clinica.recall.service.ProcedimentoPacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/procedimentos-paciente")
@RequiredArgsConstructor
public class ProcedimentoPacienteController {

    private final ProcedimentoPacienteService procedimentoPacienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<ProcedimentoPacienteResponse> registrar(
            @Valid @RequestBody ProcedimentoPacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(procedimentoPacienteService.registrar(request));
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<List<ProcedimentoPacienteResponse>> listarPorPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(procedimentoPacienteService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/hoje")
    @PreAuthorize("hasAnyRole('MEDICO', 'SECRETARIA')")
    public ResponseEntity<List<ProcedimentoPacienteResponse>> listarParaContactarHoje() {
        return ResponseEntity.ok(procedimentoPacienteService.listarParaContactarHoje());
    }
}