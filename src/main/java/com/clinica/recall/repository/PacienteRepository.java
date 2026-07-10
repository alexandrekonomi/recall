package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByAtivoTrue();
    Optional<Paciente> findByTelefone(String telefone);
}