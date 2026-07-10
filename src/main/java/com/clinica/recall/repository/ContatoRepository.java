package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.Contato;
import com.clinica.recall.domain.enums.ResultadoContato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    List<Contato> findByPacienteId(Long pacienteId);

    Optional<Contato> findByProcedimentoPacienteIdAndStatus(
            Long procedimentoPacienteId, ResultadoContato status);
}