package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProcedimentoPacienteResponse {
    private UUID id;
    private UUID pacienteId;
    private String pacienteNome;
    private UUID procedimentoId;
    private String procedimentoNome;
    private LocalDate dataRealizacao;
    private LocalDate dataProximoContato;
    private LocalDateTime criadoEm;
}