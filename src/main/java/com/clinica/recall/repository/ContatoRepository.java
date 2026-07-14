package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.Contato;
import com.clinica.recall.domain.enums.ResultadoContato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    List<Contato> findByPacienteId(Long pacienteId);

    Optional<Contato> findByProcedimentoPacienteIdAndStatus(
            Long procedimentoPacienteId, ResultadoContato status);


    @Query("""
                SELECT COUNT(c) FROM Contato c
                WHERE c.realizadoEm >= :inicioDia AND c.realizadoEm < :fimDia
            """)
    long contarContatosHoje(LocalDateTime inicioDia, LocalDateTime fimDia);

    @Query("""
                SELECT COUNT(c) FROM Contato c
                WHERE c.status = com.clinica.recall.domain.enums.ResultadoContato.AGENDOU
                AND c.realizadoEm >= :inicioDia AND c.realizadoEm < :fimDia
            """)
    long contarAgendamentosHoje(LocalDateTime inicioDia, LocalDateTime fimDia);

    @Query("SELECT DISTINCT c FROM Contato c " +
            "JOIN FETCH c.procedimentoPaciente pp " +
            "JOIN FETCH pp.paciente " +
            "JOIN FETCH pp.procedimento " +
            "WHERE c.paciente.id = :pacienteId")
    List<Contato> findByPacienteIdWithAssociations(@Param("pacienteId") Long pacienteId);

    @Query("""
                SELECT COUNT(c) FROM Contato c
                WHERE c.status = com.clinica.recall.domain.enums.ResultadoContato.AGENDOU
                AND c.realizadoEm >= :inicioMes
            """)
    long contarAgendamentosMes(LocalDateTime inicioMes);

    @Query("""
                SELECT COUNT(c) FROM Contato c
                WHERE c.realizadoEm >= :inicioMes
            """)
    long contarContatosMes(LocalDateTime inicioMes);

    @Query(value = """
                SELECT TO_CHAR(DATE_TRUNC('week', realizado_em), 'DD/MM') as semana, COUNT(*) as total
                FROM contatos
                WHERE realizado_em >= :inicioMes
                GROUP BY DATE_TRUNC('week', realizado_em)
                ORDER BY DATE_TRUNC('week', realizado_em)
            """, nativeQuery = true)
    List<Object[]> contatosPorSemanaRaw(LocalDateTime inicioMes);

    @Query("""
                SELECT c.status, COUNT(c) FROM Contato c
                WHERE c.realizadoEm >= :inicioMes
                GROUP BY c.status
            """)
    List<Object[]> distribuicaoResultadosRaw(LocalDateTime inicioMes);

    @Query(value = """
                SELECT p.nome,
                       COUNT(CASE WHEN c.status = 'RECUSOU' THEN 1 END) as recusou,
                       COUNT(c.id) as total
                FROM contatos c
                JOIN procedimentos_paciente pp ON c.procedimento_paciente_id = pp.id
                JOIN procedimentos p ON pp.procedimento_id = p.id
                WHERE c.realizado_em >= :inicioMes
                GROUP BY p.nome
                HAVING COUNT(c.id) > 0
                ORDER BY (COUNT(CASE WHEN c.status = 'RECUSOU' THEN 1 END)::float / COUNT(c.id)) DESC
                LIMIT 5
            """, nativeQuery = true)
    List<Object[]> procedimentosAbandonoRaw(LocalDateTime inicioMes);
}