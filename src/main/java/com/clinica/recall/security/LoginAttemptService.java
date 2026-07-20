package com.clinica.recall.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoginAttemptService {

    private static final Logger log = LoggerFactory.getLogger(LoginAttemptService.class);

    private static final int MAX_TENTATIVAS = 5;
    private static final long BLOQUEIO_MINUTOS = 15;

    private final Cache<String, AtomicInteger> tentativas = Caffeine.newBuilder()
            .expireAfterWrite(BLOQUEIO_MINUTOS, TimeUnit.MINUTES)
            .build();

    public void registrarFalha(String email) {
        String chave = email.toLowerCase();
        AtomicInteger contador = tentativas.get(chave, key -> new AtomicInteger(0));
        int total = contador.incrementAndGet();
        log.warn("Falha de login registrada para '{}'. Tentativa {} de {}.", chave, total, MAX_TENTATIVAS);

        if (total >= MAX_TENTATIVAS) {
            log.warn("Conta '{}' atingiu o limite de tentativas e será bloqueada por {} minutos.", chave, BLOQUEIO_MINUTOS);
        }
    }

    public void registrarSucesso(String email) {
        String chave = email.toLowerCase();
        AtomicInteger contadorAnterior = tentativas.getIfPresent(chave);
        if (contadorAnterior != null) {
            log.info("Login bem-sucedido para '{}' apos {} tentativa(s) falha(s). Contador zerado.", chave, contadorAnterior.get());
        } else {
            log.debug("Login bem-sucedido para '{}' sem tentativas falhas anteriores.", chave);
        }
        tentativas.invalidate(chave);
    }

    public boolean estaBloqueado(String email) {
        String chave = email.toLowerCase();
        AtomicInteger contador = tentativas.getIfPresent(chave);
        boolean bloqueado = contador != null && contador.get() >= MAX_TENTATIVAS;

        if (bloqueado) {
            log.warn("Tentativa de login bloqueada para '{}'. Contador atual: {}.", chave, contador.get());
        } else {
            log.debug("Verificacao de bloqueio para '{}': contador atual = {}, bloqueado = {}.",
                    chave, contador != null ? contador.get() : 0, bloqueado);
        }

        return bloqueado;
    }

    public int tentativasRestantes(String email) {
        String chave = email.toLowerCase();
        AtomicInteger contador = tentativas.getIfPresent(chave);
        int usadas = contador != null ? contador.get() : 0;
        return Math.max(0, MAX_TENTATIVAS - usadas);
    }
}