package com.clinica.recall.dto.response;

import com.clinica.recall.domain.enums.RolePerfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private RolePerfil perfil;
    private boolean ativo;
    private boolean contaAtivada;
}