package com.clinica.recall.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProcedimentoPacienteRequest {

    @NotNull
    private UUID pacienteId;

    @NotNull
    private UUID procedimentoId;

    @NotNull
    private LocalDate dataRealizacao;
}