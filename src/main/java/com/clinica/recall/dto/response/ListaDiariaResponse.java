package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ListaDiariaResponse {
    private Long procedimentoPacienteId;
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String procedimentoNome;
    private LocalDate dataProximoContato;
    private String mensagemSugerida;
}