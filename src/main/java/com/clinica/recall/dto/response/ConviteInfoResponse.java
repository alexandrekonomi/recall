package com.clinica.recall.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConviteInfoResponse {
    private String nome;
    private String email;
    private String perfil;
    private String nomeClinica;
    private boolean valido;
}