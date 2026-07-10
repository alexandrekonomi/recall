package com.clinica.recall.domain.entity;

import com.clinica.recall.domain.enums.ResultadoContato;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contatos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedimento_paciente_id", nullable = false)
    private ProcedimentoPaciente procedimentoPaciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realizado_por_id")
    private Usuario realizadoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ResultadoContato status = ResultadoContato.PENDENTE;

    @Column(name = "mensagem_enviada", columnDefinition = "TEXT")
    private String mensagemEnviada;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "realizado_em", updatable = false)
    private LocalDateTime realizadoEm;

    @Column(name = "proximo_contato_em")
    private LocalDateTime proximoContatoEm;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = ResultadoContato.PENDENTE;
        }
    }
}