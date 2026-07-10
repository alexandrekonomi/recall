package com.clinica.recall.dto.request;

import com.clinica.recall.domain.enums.TagPaciente;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PacienteRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String telefone;

    private LocalDate dataNascimento;

    private List<TagPaciente> tags;
}