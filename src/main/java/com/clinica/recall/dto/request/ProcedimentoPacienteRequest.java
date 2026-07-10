package com.clinica.recall.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProcedimentoPacienteRequest {

    @NotNull
    private Long pacienteId;

    @NotNull
    private Long procedimentoId;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataRealizacao;
}