CREATE TABLE configuracoes_clinica (
    id BIGSERIAL PRIMARY KEY,
    nome_clinica VARCHAR(255) NOT NULL DEFAULT 'Minha Clínica',
    telefone_contato VARCHAR(20),
    dias_recontato_sem_resposta INTEGER NOT NULL DEFAULT 3,
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO configuracoes_clinica (nome_clinica, telefone_contato, dias_recontato_sem_resposta)
VALUES ('agapelle', '(11) 4522-8890', 3);