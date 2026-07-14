package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProcedimentoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Integer intervaloRetornoDias;
    private String templateMensagem;
    private boolean ativo;
    private BigDecimal valor;
}