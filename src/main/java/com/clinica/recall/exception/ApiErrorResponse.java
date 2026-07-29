package com.clinica.recall.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String codigo;
    private String mensagem;
    private String path;
    private String traceId;
    private List<CampoInvalido> camposInvalidos;

    @Data
    @Builder
    public static class CampoInvalido {
        private String campo;
        private String mensagem;
    }
}