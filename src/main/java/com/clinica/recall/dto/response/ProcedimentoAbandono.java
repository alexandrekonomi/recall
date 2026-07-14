package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProcedimentoAbandono {
    private String procedimentoNome;
    private long totalRecusou;
    private long totalContatos;
    private double taxaAbandono;
}