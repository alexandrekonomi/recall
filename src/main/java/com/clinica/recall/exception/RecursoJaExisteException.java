package com.clinica.recall.exception;

import org.springframework.http.HttpStatus;

public class RecursoJaExisteException extends BusinessException {
    public RecursoJaExisteException(String mensagem) {
        super(mensagem, HttpStatus.CONFLICT, "RECURSO_JA_EXISTE");
    }
}