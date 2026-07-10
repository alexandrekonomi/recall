package com.clinica.recall.dto.response;

import com.clinica.recall.domain.enums.TagPaciente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PacienteResponse {
    private UUID id;
    private String nome;
    private String telefone;
    private LocalDate dataNascimento;
    private List<TagPaciente> tags;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}