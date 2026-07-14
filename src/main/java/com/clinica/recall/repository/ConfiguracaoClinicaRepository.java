package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.ConfiguracaoClinica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoClinicaRepository extends JpaRepository<ConfiguracaoClinica, Long> {
}