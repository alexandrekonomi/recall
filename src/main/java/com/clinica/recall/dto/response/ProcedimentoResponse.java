package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProcedimentoResponse {
    private UUID id;
    private String nome;
    private String descricao;
    private Integer intervaloRetornoDias;
    private String templateMensagem;
    private boolean ativo;
}