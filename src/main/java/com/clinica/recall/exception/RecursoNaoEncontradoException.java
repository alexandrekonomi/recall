package com.clinica.recall.exception;

import org.springframework.http.HttpStatus;

public class RecursoNaoEncontradoException extends BusinessException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem, HttpStatus.NOT_FOUND, "RECURSO_NAO_ENCONTRADO");
    }
}