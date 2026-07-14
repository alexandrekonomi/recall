package com.clinica.recall.service;

import com.clinica.recall.domain.entity.ConfiguracaoClinica;
import com.clinica.recall.dto.request.ConfiguracaoClinicaRequest;
import com.clinica.recall.dto.response.ConfiguracaoClinicaResponse;
import com.clinica.recall.repository.ConfiguracaoClinicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfiguracaoClinicaService {

    private final ConfiguracaoClinicaRepository repository;

    public ConfiguracaoClinicaResponse buscar() {
        ConfiguracaoClinica config = repository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Configuração não encontrada"));
        return toResponse(config);
    }

    public ConfiguracaoClinicaResponse atualizar(ConfiguracaoClinicaRequest request) {
        ConfiguracaoClinica config = repository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Configuração não encontrada"));

        config.setNomeClinica(request.getNomeClinica());
        config.setTelefoneContato(request.getTelefoneContato());
        config.setDiasRecontatoSemResposta(request.getDiasRecontatoSemResposta());

        return toResponse(repository.save(config));
    }

    private ConfiguracaoClinicaResponse toResponse(ConfiguracaoClinica c) {
        return ConfiguracaoClinicaResponse.builder()
                .id(c.getId())
                .nomeClinica(c.getNomeClinica())
                .telefoneContato(c.getTelefoneContato())
                .diasRecontatoSemResposta(c.getDiasRecontatoSemResposta())
                .build();
    }
}