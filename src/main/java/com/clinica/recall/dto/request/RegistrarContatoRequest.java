package com.clinica.recall.dto.request;

import com.clinica.recall.domain.enums.ResultadoContato;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RegistrarContatoRequest {

    @NotNull
    private Long procedimentoPacienteId;

    @NotNull
    private ResultadoContato status;

    private String observacao;
}