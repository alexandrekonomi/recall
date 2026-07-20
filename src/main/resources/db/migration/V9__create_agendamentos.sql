CREATE TABLE agendamentos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    procedimento_id BIGINT,
    data_agendada DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'AGUARDANDO_DADOS',
    contato_origem_id BIGINT NOT NULL,
    procedimento_paciente_id BIGINT,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP,
    CONSTRAINT fk_agendamento_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_agendamento_procedimento FOREIGN KEY (procedimento_id) REFERENCES procedimentos(id),
    CONSTRAINT fk_agendamento_contato FOREIGN KEY (contato_origem_id) REFERENCES contatos(id),
    CONSTRAINT fk_agendamento_procedimento_paciente FOREIGN KEY (procedimento_paciente_id) REFERENCES procedimentos_paciente(id)
);

CREATE INDEX idx_agendamentos_status ON agendamentos(status);
CREATE INDEX idx_agendamentos_paciente ON agendamentos(paciente_id);