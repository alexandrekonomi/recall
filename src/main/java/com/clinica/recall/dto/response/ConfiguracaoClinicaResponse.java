package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConfiguracaoClinicaResponse {
    private Long id;
    private String nomeClinica;
    private String telefoneContato;
    private Integer diasRecontatoSemResposta;
}