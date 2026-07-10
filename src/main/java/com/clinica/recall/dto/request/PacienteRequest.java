package com.clinica.recall.dto.request;

import com.clinica.recall.domain.enums.TagPaciente;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    private List<TagPaciente> tags;
}