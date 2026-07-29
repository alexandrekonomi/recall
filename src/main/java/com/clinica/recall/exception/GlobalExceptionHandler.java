package com.clinica.recall.exception;

import com.clinica.recall.config.TraceIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        log.warn("Erro de negocio no endpoint {} {}: [{}] {}",
                request.getMethod(), request.getRequestURI(), ex.getCodigo(), ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatus().value())
                .erro(ex.getStatus().getReasonPhrase())
                .codigo(ex.getCodigo())
                .mensagem(ex.getMessage())
                .path(request.getRequestURI())
                .traceId(MDC.get(TraceIdFilter.TRACE_ID_KEY))
                .build();

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ApiErrorResponse.CampoInvalido> campos = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> ApiErrorResponse.CampoInvalido.builder()
                        .campo(fe.getField())
                        .mensagem(fe.getDefaultMessage())
                        .build())
                .toList();

        log.warn("Erro de validacao no endpoint {} {}: {} campo(s) invalido(s)",
                request.getMethod(), request.getRequestURI(), campos.size());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Bad Request")
                .codigo("DADOS_INVALIDOS")
                .mensagem("Verifique os campos preenchidos")
                .path(request.getRequestURI())
                .traceId(MDC.get(TraceIdFilter.TRACE_ID_KEY))
                .camposInvalidos(campos)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException ex, HttpServletRequest request) {

        log.warn("Falha de autenticacao no endpoint {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .erro("Unauthorized")
                .codigo("CREDENCIAIS_INVALIDAS")
                .mensagem("E-mail ou senha incorretos")
                .path(request.getRequestURI())
                .traceId(MDC.get(TraceIdFilter.TRACE_ID_KEY))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        String traceId = MDC.get(TraceIdFilter.TRACE_ID_KEY);

        log.error("Erro nao tratado no endpoint {} {} [traceId={}]: {}",
                request.getMethod(), request.getRequestURI(), traceId, ex.getMessage(), ex);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .erro("Internal Server Error")
                .codigo("ERRO_INTERNO")
                .mensagem("Ocorreu um erro inesperado. Se persistir, contate o suporte informando o codigo " + traceId)
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}