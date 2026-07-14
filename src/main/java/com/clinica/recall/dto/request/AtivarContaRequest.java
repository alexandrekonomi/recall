package com.clinica.recall.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AtivarContaRequest {
    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String senha;
}