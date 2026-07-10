package com.clinica.recall.domain.entity;

import com.clinica.recall.domain.enums.TagPaciente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pacientes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @ElementCollection(targetClass = TagPaciente.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "paciente_tags", joinColumns = @JoinColumn(name = "paciente_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tag")
    @Builder.Default
    private List<TagPaciente> tags = new ArrayList<>();

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProcedimentoPaciente> procedimentos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}