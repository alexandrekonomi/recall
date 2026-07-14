package com.clinica.recall.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfiguracaoClinicaRequest {

    @NotBlank
    private String nomeClinica;

    private String telefoneContato;

    @NotNull
    private Integer diasRecontatoSemResposta;
}