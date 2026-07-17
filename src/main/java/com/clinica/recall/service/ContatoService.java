package com.clinica.recall.service;

import com.clinica.recall.domain.entity.Contato;
import com.clinica.recall.domain.entity.ProcedimentoPaciente;
import com.clinica.recall.domain.entity.Usuario;
import com.clinica.recall.domain.enums.ResultadoContato;
import com.clinica.recall.dto.request.RegistrarContatoRequest;
import com.clinica.recall.dto.response.ContatoResponse;
import com.clinica.recall.dto.response.ListaDiariaResponse;
import com.clinica.recall.dto.response.MetricasDashboardResponse;
import com.clinica.recall.repository.ContatoRepository;
import com.clinica.recall.repository.ProcedimentoPacienteRepository;
import com.clinica.recall.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final ProcedimentoPacienteRepository procedimentoPacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final AgendamentoService agendamentoService;

    @Transactional(readOnly = true)
    public List<ListaDiariaResponse> listarParaContactarHoje() {
        return procedimentoPacienteRepository
                .findPacientesParaContactar(LocalDate.now())
                .stream()
                .map(pp -> ListaDiariaResponse.builder()
                        .procedimentoPacienteId(pp.getId())
                        .pacienteId(pp.getPaciente().getId())
                        .pacienteNome(pp.getPaciente().getNome())
                        .pacienteTelefone(pp.getPaciente().getTelefone())
                        .procedimentoNome(pp.getProcedimento().getNome())
                        .dataProximoContato(pp.getDataProximoContato())
                        .mensagemSugerida(montarMensagem(pp))
                        .build())
                .toList();
    }

    @Transactional
    public ContatoResponse registrarResultado(RegistrarContatoRequest request) {
        ProcedimentoPaciente pp = procedimentoPacienteRepository
                .findByIdWithProcedimento(request.getProcedimentoPacienteId())
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));

        Usuario secretaria = usuarioLogado();

        Contato contato = contatoRepository
                .findByProcedimentoPacienteIdAndStatus(pp.getId(), ResultadoContato.PENDENTE)
                .orElse(Contato.builder()
                        .paciente(pp.getPaciente())
                        .procedimentoPaciente(pp)
                        .build());

        contato.setStatus(request.getStatus());
        contato.setObservacao(request.getObservacao());
        contato.setRealizadoPor(secretaria);
        contato.setRealizadoEm(LocalDateTime.now());
        contato.setMensagemEnviada(montarMensagem(pp));

        if (request.getStatus() == ResultadoContato.SEM_RESPOSTA) {
            contato.setProximoContatoEm(LocalDateTime.now().plusDays(3));
        }

        Contato salvo = contatoRepository.save(contato);

        if (request.getStatus() == ResultadoContato.AGENDOU) {
            agendamentoService.criarAPartirDoContato(
                    salvo,
                    request.getProcedimentoAgendadoId(),
                    request.getDataAgendada()
            );
        }

        return toResponse(salvo, pp);
    }

    @Transactional(readOnly = true)
    public List<ContatoResponse> listarPorPaciente(Long pacienteId) {
        return contatoRepository.findByPacienteIdWithAssociations(pacienteId)
                .stream()
                .map(c -> toResponse(c, c.getProcedimentoPaciente()))
                .toList();
    }

    private String montarMensagem(ProcedimentoPaciente pp) {
        String template = pp.getProcedimento().getTemplateMensagem();

        if (template == null || template.isBlank()) {
            return "Olá " + pp.getPaciente().getNome() +
                    ", tudo bem? Gostaríamos de agendar seu retorno para " +
                    pp.getProcedimento().getNome() + ". Entre em contato conosco!";
        }

        return template
                .replace("{nome}", pp.getPaciente().getNome())
                .replace("{procedimento}", pp.getProcedimento().getNome())
                .replace("{data}", pp.getDataProximoContato().toString());
    }

    private Usuario usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private ContatoResponse toResponse(Contato c, ProcedimentoPaciente pp) {
        return ContatoResponse.builder()
                .id(c.getId())
                .pacienteId(pp.getPaciente().getId())
                .pacienteNome(pp.getPaciente().getNome())
                .pacienteTelefone(pp.getPaciente().getTelefone())
                .procedimentoPacienteId(pp.getId())
                .procedimentoNome(pp.getProcedimento().getNome())
                .mensagemSugerida(montarMensagem(pp))
                .status(c.getStatus())
                .observacao(c.getObservacao())
                .realizadoEm(c.getRealizadoEm())
                .proximoContatoEm(c.getProximoContatoEm())
                .build();
    }

    public MetricasDashboardResponse buscarMetricasHoje() {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDia = inicioDia.plusDays(1);

        long aContactar = procedimentoPacienteRepository
                .findPacientesParaContactar(LocalDate.now())
                .size();

        long contactados = contatoRepository.contarContatosHoje(inicioDia, fimDia);
        long agendamentos = contatoRepository.contarAgendamentosHoje(inicioDia, fimDia);

        return MetricasDashboardResponse.builder()
                .pacientesAContactarHoje(aContactar)
                .contactadosHoje(contactados)
                .agendamentosHoje(agendamentos)
                .build();
    }
}