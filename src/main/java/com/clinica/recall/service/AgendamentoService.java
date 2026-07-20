package com.clinica.recall.service;

import com.clinica.recall.domain.entity.Agendamento;
import com.clinica.recall.domain.entity.Contato;
import com.clinica.recall.domain.entity.Procedimento;
import com.clinica.recall.domain.entity.ProcedimentoPaciente;
import com.clinica.recall.domain.enums.StatusAgendamento;
import com.clinica.recall.dto.response.AgendamentoResponse;
import com.clinica.recall.repository.AgendamentoRepository;
import com.clinica.recall.repository.ProcedimentoPacienteRepository;
import com.clinica.recall.repository.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final ProcedimentoPacienteRepository procedimentoPacienteRepository;

    public Agendamento criarAPartirDoContato(Contato contato, Long procedimentoAgendadoId, LocalDate dataAgendada) {
        Procedimento procedimento = null;
        if (procedimentoAgendadoId != null) {
            procedimento = procedimentoRepository.findById(procedimentoAgendadoId)
                    .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
        }

        Agendamento agendamento = Agendamento.builder()
                .paciente(contato.getPaciente())
                .procedimento(procedimento)
                .dataAgendada(dataAgendada)
                .contatoOrigem(contato)
                .build();

        return agendamentoRepository.save(agendamento);
    }


    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarAguardandoDados() {
        return agendamentoRepository.findByStatusWithAssociations(StatusAgendamento.AGUARDANDO_DADOS)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AgendamentoResponse> listarAguardandoRealizacao() {
        return agendamentoRepository.findByStatusWithAssociations(StatusAgendamento.AGUARDANDO_REALIZACAO)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AgendamentoResponse toResponse(Agendamento agendamento) {
        // Agora paciente e procedimento já estão inicializados
        return AgendamentoResponse.builder()
                .id(agendamento.getId())
                .pacienteId(agendamento.getPaciente().getId())
                .pacienteNome(agendamento.getPaciente().getNome())
                .pacienteTelefone(agendamento.getPaciente().getTelefone())
                .procedimentoId(agendamento.getProcedimento() != null ? agendamento.getProcedimento().getId() : null)
                .procedimentoNome(agendamento.getProcedimento() != null ? agendamento.getProcedimento().getNome() : null)
                .dataAgendada(agendamento.getDataAgendada())
                .status(agendamento.getStatus())
                .criadoEm(agendamento.getCriadoEm())
                .build();
    }

    @Transactional
    public AgendamentoResponse completar(Long id, Long procedimentoId, LocalDate dataAgendada) {

        Agendamento agendamento = agendamentoRepository
                .findByIdWithAssociations(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        Procedimento procedimento = procedimentoRepository
                .findById(procedimentoId)
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));

        agendamento.setProcedimento(procedimento);
        agendamento.setDataAgendada(dataAgendada);
        agendamento.setStatus(StatusAgendamento.AGUARDANDO_REALIZACAO);

        agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse confirmarRealizacao(Long id, Long procedimentoIdConfirmado, LocalDate dataRealizacao) {
        // Usa a consulta que já carrega paciente e procedimento
        Agendamento agendamento = agendamentoRepository
                .findByIdWithAssociations(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (agendamento.getStatus() != StatusAgendamento.AGUARDANDO_REALIZACAO) {
            throw new RuntimeException("Este agendamento não está aguardando realização");
        }

        Procedimento procedimento = agendamento.getProcedimento();
        if (procedimento == null) {
            if (procedimentoIdConfirmado == null) {
                throw new RuntimeException("Procedimento não foi informado");
            }
            procedimento = procedimentoRepository
                    .findById(procedimentoIdConfirmado)
                    .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
        }

        LocalDate dataFinal = dataRealizacao != null ? dataRealizacao : LocalDate.now();
        LocalDate proximoContato = dataFinal.plusDays(procedimento.getIntervaloRetornoDias());

        ProcedimentoPaciente procedimentoPaciente = ProcedimentoPaciente.builder()
                .paciente(agendamento.getPaciente()) // paciente já carregado
                .procedimento(procedimento)          // procedimento já carregado ou recém-buscado
                .dataRealizacao(dataFinal)
                .dataProximoContato(proximoContato)
                .build();

        procedimentoPaciente = procedimentoPacienteRepository.save(procedimentoPaciente);

        agendamento.setProcedimento(procedimento);
        agendamento.setProcedimentoPaciente(procedimentoPaciente);
        agendamento.setStatus(StatusAgendamento.REALIZADO);

        agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

}