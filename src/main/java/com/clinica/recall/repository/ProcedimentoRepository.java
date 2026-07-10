package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, UUID> {
    List<Procedimento> findByAtivoTrue();
}