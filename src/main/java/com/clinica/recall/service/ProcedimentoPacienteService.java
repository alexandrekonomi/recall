package com.clinica.recall.service;

import com.clinica.recall.domain.entity.Paciente;
import com.clinica.recall.domain.entity.Procedimento;
import com.clinica.recall.domain.entity.ProcedimentoPaciente;
import com.clinica.recall.domain.entity.Usuario;
import com.clinica.recall.dto.request.ProcedimentoPacienteRequest;
import com.clinica.recall.dto.response.ProcedimentoPacienteResponse;
import com.clinica.recall.repository.PacienteRepository;
import com.clinica.recall.repository.ProcedimentoPacienteRepository;
import com.clinica.recall.repository.ProcedimentoRepository;
import com.clinica.recall.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcedimentoPacienteService {

    private final ProcedimentoPacienteRepository procedimentoPacienteRepository;
    private final PacienteRepository pacienteRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProcedimentoPacienteResponse registrar(ProcedimentoPacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Procedimento procedimento = procedimentoRepository.findById(request.getProcedimentoId())
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));

        Usuario usuarioLogado = usuarioLogado();

        LocalDate dataProximoContato = request.getDataRealizacao()
                .plusDays(procedimento.getIntervaloRetornoDias());

        ProcedimentoPaciente pp = ProcedimentoPaciente.builder()
                .paciente(paciente)
                .procedimento(procedimento)
                .dataRealizacao(request.getDataRealizacao())
                .dataProximoContato(dataProximoContato)
                .registradoPorId(usuarioLogado.getId())
                .build();

        return toResponse(procedimentoPacienteRepository.save(pp));
    }

    public List<ProcedimentoPacienteResponse> listarPorPaciente(Long pacienteId) {
        return procedimentoPacienteRepository.findByPacienteId(pacienteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProcedimentoPacienteResponse> listarParaContactarHoje() {
        return procedimentoPacienteRepository
                .findPacientesParaContactar(LocalDate.now())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Usuario usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private ProcedimentoPacienteResponse toResponse(ProcedimentoPaciente pp) {
        return ProcedimentoPacienteResponse.builder()
                .id(pp.getId())
                .pacienteId(pp.getPaciente().getId())
                .pacienteNome(pp.getPaciente().getNome())
                .procedimentoId(pp.getProcedimento().getId())
                .procedimentoNome(pp.getProcedimento().getNome())
                .dataRealizacao(pp.getDataRealizacao())
                .dataProximoContato(pp.getDataProximoContato())
                .criadoEm(pp.getCriadoEm())
                .build();
    }
}