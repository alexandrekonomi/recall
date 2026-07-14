package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PainelGestaoResponse {
    private long pacientesAtivos;
    private long pacientesInativos;
    private double taxaRetornoMes;
    private long totalContatosMes;
    private BigDecimal receitaPotencial;
    private List<ContatosPorSemana> contatosPorSemana;
    private List<DistribuicaoResultado> distribuicaoResultados;
    private List<ProcedimentoAbandono> procedimentosComMaiorAbandono;
}