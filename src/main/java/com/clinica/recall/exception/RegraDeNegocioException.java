package com.clinica.recall.exception;

import org.springframework.http.HttpStatus;

public class RegraDeNegocioException extends BusinessException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem, HttpStatus.UNPROCESSABLE_ENTITY, "REGRA_DE_NEGOCIO");
    }
}