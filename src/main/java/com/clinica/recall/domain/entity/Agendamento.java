package com.clinica.recall.domain.entity;

import com.clinica.recall.domain.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedimento_id")
    private Procedimento procedimento;

    @Column(name = "data_agendada")
    private LocalDate dataAgendada;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private StatusAgendamento status = StatusAgendamento.AGUARDANDO_DADOS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contato_origem_id", nullable = false)
    private Contato contatoOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedimento_paciente_id")
    private ProcedimentoPaciente procedimentoPaciente;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        if (this.status == null) {
            this.status = definirStatusInicial();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    private StatusAgendamento definirStatusInicial() {
        return (this.procedimento != null && this.dataAgendada != null)
                ? StatusAgendamento.AGUARDANDO_REALIZACAO
                : StatusAgendamento.AGUARDANDO_DADOS;
    }
}