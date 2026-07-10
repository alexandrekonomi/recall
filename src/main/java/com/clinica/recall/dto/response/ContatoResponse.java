package com.clinica.recall.dto.response;

import com.clinica.recall.domain.enums.ResultadoContato;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ContatoResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private Long procedimentoPacienteId;
    private String procedimentoNome;
    private String mensagemSugerida;
    private ResultadoContato status;
    private String observacao;
    private LocalDateTime realizadoEm;
    private LocalDateTime proximoContatoEm;
}