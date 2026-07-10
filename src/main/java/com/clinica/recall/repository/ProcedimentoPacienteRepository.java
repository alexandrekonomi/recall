package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.ProcedimentoPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProcedimentoPacienteRepository extends JpaRepository<ProcedimentoPaciente, UUID> {

    List<ProcedimentoPaciente> findByPacienteId(UUID pacienteId);

    @Query("""
        SELECT pp FROM ProcedimentoPaciente pp
        WHERE pp.dataProximoContato <= :data
        AND NOT EXISTS (
            SELECT c FROM Contato c
            WHERE c.procedimentoPaciente = pp
            AND c.status IN (
                com.clinica.recall.domain.enums.ResultadoContato.AGENDOU,
                com.clinica.recall.domain.enums.ResultadoContato.RECUSOU
            )
        )
        ORDER BY pp.dataProximoContato ASC
    """)
    List<ProcedimentoPaciente> findPacientesParaContactar(LocalDate data);
}