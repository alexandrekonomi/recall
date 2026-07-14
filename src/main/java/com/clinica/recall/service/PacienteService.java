package com.clinica.recall.service;

import com.clinica.recall.domain.entity.Paciente;
import com.clinica.recall.dto.request.PacienteRequest;
import com.clinica.recall.dto.response.ContatoResponse;
import com.clinica.recall.dto.response.PacienteResponse;
import com.clinica.recall.dto.response.PerfilPacienteResponse;
import com.clinica.recall.dto.response.ProcedimentoPacienteResponse;
import com.clinica.recall.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ContatoService contatoService;
    private final ProcedimentoPacienteService procedimentoPacienteService;

    public PacienteResponse criar(PacienteRequest request) {
        pacienteRepository.findByTelefone(request.getTelefone()).ifPresent(p -> {
            throw new RuntimeException("Telefone já cadastrado");
        });

        Paciente paciente = Paciente.builder()
                .nome(request.getNome())
                .telefone(request.getTelefone())
                .dataNascimento(request.getDataNascimento())
                .tags(request.getTags() != null ? request.getTags() : new ArrayList<>())
                .ativo(true)
                .build();

        return toResponse(pacienteRepository.save(paciente));
    }

    public List<PacienteResponse> listar() {
        return pacienteRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    public PacienteResponse atualizar(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        paciente.setNome(request.getNome());
        paciente.setTelefone(request.getTelefone());
        paciente.setDataNascimento(request.getDataNascimento());
        if (request.getTags() != null) {
            paciente.getTags().clear();
            paciente.getTags().addAll(request.getTags());
        }

        return toResponse(pacienteRepository.save(paciente));
    }

    public void desativar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        paciente.setAtivo(false);
        pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public PerfilPacienteResponse buscarPerfil(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        List<ContatoResponse> contatos = contatoService.listarPorPaciente(pacienteId);
        List<ProcedimentoPacienteResponse> procedimentos = procedimentoPacienteService.listarPorPaciente(pacienteId);

        return PerfilPacienteResponse.builder()
                .paciente(toResponse(paciente))
                .contatos(contatos)
                .procedimentos(procedimentos)
                .build();
    }

    private PacienteResponse toResponse(Paciente p) {
        return PacienteResponse.builder()
                .id(p.getId())
                .nome(p.getNome())
                .telefone(p.getTelefone())
                .dataNascimento(p.getDataNascimento())
                .tags(p.getTags())
                .ativo(p.isAtivo())
                .criadoEm(p.getCriadoEm())
                .atualizadoEm(p.getAtualizadoEm())
                .build();
    }
}