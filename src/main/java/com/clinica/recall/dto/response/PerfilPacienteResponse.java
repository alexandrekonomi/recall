package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PerfilPacienteResponse {
    private PacienteResponse paciente;
    private List<ContatoResponse> contatos;
    private List<ProcedimentoPacienteResponse> procedimentos;
}
