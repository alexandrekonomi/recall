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

    // ContatoRepository.java
    @Query("SELECT DISTINCT c FROM Contato c " +
            "JOIN FETCH c.procedimentoPaciente pp " +
            "JOIN FETCH pp.paciente " +
            "JOIN FETCH pp.procedimento " +
            "WHERE c.paciente.id = :pacienteId")
    List<Contato> findByPacienteIdWithAssociations(@Param("pacienteId") Long pacienteId);
}