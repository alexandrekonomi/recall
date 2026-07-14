package com.clinica.recall.service;

import com.clinica.recall.dto.response.*;
import com.clinica.recall.repository.ContatoRepository;
import com.clinica.recall.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PainelGestaoService {

    private final PacienteRepository pacienteRepository;
    private final ContatoRepository contatoRepository;

    @Transactional(readOnly = true)
    public PainelGestaoResponse buscarPainel() {

        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        long pacientesAtivos = pacienteRepository.count();
        long pacientesInativos = pacienteRepository.contarPacientesInativos();

        long totalContatosMes = contatoRepository.contarContatosMes(inicioMes);
        long agendamentosMes = contatoRepository.contarAgendamentosMes(inicioMes);

        double taxaRetorno = totalContatosMes > 0
                ? (agendamentosMes * 100.0) / totalContatosMes
                : 0.0;

        List<ContatosPorSemana> contatosPorSemana = contatoRepository
                .contatosPorSemanaRaw(inicioMes)
                .stream()
                .map(row -> ContatosPorSemana.builder()
                        .semana((String) row[0])
                        .total(((Number) row[1]).longValue())
                        .build())
                .toList();

        List<DistribuicaoResultado> distribuicao = contatoRepository
                .distribuicaoResultadosRaw(inicioMes)
                .stream()
                .map(row -> DistribuicaoResultado.builder()
                        .status(row[0].toString())
                        .total(((Number) row[1]).longValue())
                        .build())
                .toList();

        List<ProcedimentoAbandono> abandono = contatoRepository
                .procedimentosAbandonoRaw(inicioMes)
                .stream()
                .map(row -> {
                    long recusou = ((Number) row[1]).longValue();
                    long total = ((Number) row[2]).longValue();
                    double taxa = total > 0 ? (recusou * 100.0) / total : 0.0;
                    return ProcedimentoAbandono.builder()
                            .procedimentoNome((String) row[0])
                            .totalRecusou(recusou)
                            .totalContatos(total)
                            .taxaAbandono(Math.round(taxa * 10) / 10.0)
                            .build();
                })
                .toList();

        BigDecimal receitaPotencial = pacienteRepository.calcularReceitaPotencial();

        return PainelGestaoResponse.builder()
                .pacientesAtivos(pacientesAtivos)
                .pacientesInativos(pacientesInativos)
                .taxaRetornoMes(Math.round(taxaRetorno * 10) / 10.0)
                .totalContatosMes(totalContatosMes)
                .receitaPotencial(receitaPotencial)
                .contatosPorSemana(contatosPorSemana)
                .distribuicaoResultados(distribuicao)
                .procedimentosComMaiorAbandono(abandono)
                .build();
    }
}