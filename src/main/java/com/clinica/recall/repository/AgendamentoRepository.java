package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.Agendamento;
import com.clinica.recall.domain.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Para o completar (carrega paciente + procedimento)
    @Query("SELECT DISTINCT a FROM Agendamento a " +
            "JOIN FETCH a.paciente " +
            "LEFT JOIN FETCH a.procedimento " +
            "WHERE a.id = :id")
    Optional<Agendamento> findByIdWithAssociations(@Param("id") Long id);

    // Para a listagem (mesma ideia)
    @Query("SELECT DISTINCT a FROM Agendamento a " +
            "JOIN FETCH a.paciente " +
            "LEFT JOIN FETCH a.procedimento " +
            "WHERE a.status = :status " +
            "ORDER BY a.criadoEm ASC")
    List<Agendamento> findByStatusWithAssociations(@Param("status") StatusAgendamento status);
}