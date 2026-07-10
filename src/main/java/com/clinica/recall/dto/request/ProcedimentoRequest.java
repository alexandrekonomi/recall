package com.clinica.recall.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProcedimentoRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotNull
    @Min(1)
    private Integer intervaloRetornoDias;

    private String templateMensagem;
}