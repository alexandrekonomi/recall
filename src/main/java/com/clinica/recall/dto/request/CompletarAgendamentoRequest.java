package com.clinica.recall.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CompletarAgendamentoRequest {
    @NotNull
    private Long procedimentoId;

    @NotNull
    private LocalDate dataAgendada;
}