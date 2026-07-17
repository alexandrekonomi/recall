package com.clinica.recall.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ConfirmarRealizacaoRequest {
    private Long procedimentoId;
    private LocalDate dataRealizacao;
}