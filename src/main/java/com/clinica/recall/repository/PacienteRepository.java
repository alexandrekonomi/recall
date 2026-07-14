package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByAtivoTrue();

    Optional<Paciente> findByTelefone(String telefone);

    @Query("""
                SELECT COUNT(DISTINCT pp.paciente.id) FROM ProcedimentoPaciente pp
                WHERE pp.dataProximoContato <= CURRENT_DATE
            """)
    long contarPacientesInativos();

    @Query(value = """
    SELECT COALESCE(SUM(p.valor), 0)
    FROM procedimentos_paciente pp
    JOIN procedimentos p ON pp.procedimento_id = p.id
    WHERE pp.data_proximo_contato <= CURRENT_DATE
""", nativeQuery = true)
    BigDecimal calcularReceitaPotencial();
}