package com.clinica.recall.dto.response;

import com.clinica.recall.domain.enums.StatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AgendamentoResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private Long procedimentoId;
    private String procedimentoNome;
    private LocalDate dataAgendada;
    private StatusAgendamento status;
    private LocalDateTime criadoEm;
}