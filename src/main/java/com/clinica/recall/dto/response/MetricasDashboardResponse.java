package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MetricasDashboardResponse {
    private long pacientesAContactarHoje;
    private long contactadosHoje;
    private long agendamentosHoje;
}