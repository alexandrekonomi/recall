package com.clinica.recall.controller;

import com.clinica.recall.dto.response.PainelGestaoResponse;
import com.clinica.recall.service.PainelGestaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/painel-gestao")
@RequiredArgsConstructor
@Slf4j
public class PainelGestaoController {

    private final PainelGestaoService painelGestaoService;

    @GetMapping
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<PainelGestaoResponse> buscarPainel() {
        log.info("Buscando Painel Gestao");
        return ResponseEntity.ok(painelGestaoService.buscarPainel());
    }
}