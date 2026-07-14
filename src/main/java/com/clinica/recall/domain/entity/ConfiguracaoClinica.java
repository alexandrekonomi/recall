package com.clinica.recall.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuracoes_clinica")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_clinica", nullable = false)
    private String nomeClinica;

    @Column(name = "telefone_contato")
    private String telefoneContato;

    @Column(name = "dias_recontato_sem_resposta", nullable = false)
    private Integer diasRecontatoSemResposta;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}