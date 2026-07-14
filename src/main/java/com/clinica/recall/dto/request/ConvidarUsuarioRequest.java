package com.clinica.recall.dto.request;

import com.clinica.recall.domain.enums.RolePerfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConvidarUsuarioRequest {
    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private RolePerfil perfil;
}