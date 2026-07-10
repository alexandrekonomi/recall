package com.clinica.recall.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "procedimentos_paciente")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedimentoPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedimento_id", nullable = false)
    private Procedimento procedimento;

    @Column(name = "data_realizacao", nullable = false)
    private LocalDate dataRealizacao;

    @Column(name = "data_proximo_contato", nullable = false)
    private LocalDate dataProximoContato;

    @Column(name = "registrado_por_id")
    private Long registradoPorId;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        if (this.procedimento != null && this.dataRealizacao != null) {
            this.dataProximoContato = this.dataRealizacao
                    .plusDays(this.procedimento.getIntervaloRetornoDias());
        }
    }
}