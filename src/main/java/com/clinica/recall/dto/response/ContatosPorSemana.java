package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContatosPorSemana {
    private String semana;
    private long total;
}