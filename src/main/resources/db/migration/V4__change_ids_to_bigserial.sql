-- Remover constraints de FK antes de alterar
ALTER TABLE procedimentos DROP CONSTRAINT fk_procedimento_usuario;
ALTER TABLE paciente_tags DROP CONSTRAINT fk_tag_paciente;
ALTER TABLE procedimentos_paciente DROP CONSTRAINT fk_pp_paciente;
ALTER TABLE procedimentos_paciente DROP CONSTRAINT fk_pp_procedimento;
ALTER TABLE contatos DROP CONSTRAINT fk_contato_paciente;
ALTER TABLE contatos DROP CONSTRAINT fk_contato_pp;
ALTER TABLE contatos DROP CONSTRAINT fk_contato_usuario;

-- Dropar tabelas dependentes primeiro
DROP TABLE IF EXISTS contatos;
DROP TABLE IF EXISTS procedimentos_paciente;
DROP TABLE IF EXISTS paciente_tags;
DROP TABLE IF EXISTS procedimentos;
DROP TABLE IF EXISTS pacientes;
DROP TABLE IF EXISTS usuarios;

-- Recriar com BIGSERIAL
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL
);

CREATE TABLE procedimentos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    intervalo_retorno_dias INTEGER NOT NULL,
    template_mensagem TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_por_id BIGINT NOT NULL,
    CONSTRAINT fk_procedimento_usuario FOREIGN KEY (criado_por_id) REFERENCES usuarios(id)
);

CREATE TABLE pacientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL UNIQUE,
    data_nascimento DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP
);

CREATE TABLE paciente_tags (
    paciente_id BIGINT NOT NULL,
    tag VARCHAR(20) NOT NULL,
    CONSTRAINT fk_tag_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    PRIMARY KEY (paciente_id, tag)
);

CREATE TABLE procedimentos_paciente (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    procedimento_id BIGINT NOT NULL,
    data_realizacao DATE NOT NULL,
    data_proximo_contato DATE NOT NULL,
    registrado_por_id BIGINT,
    criado_em TIMESTAMP NOT NULL,
    CONSTRAINT fk_pp_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_pp_procedimento FOREIGN KEY (procedimento_id) REFERENCES procedimentos(id)
);

CREATE TABLE contatos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    procedimento_paciente_id BIGINT NOT NULL,
    realizado_por_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    mensagem_enviada TEXT,
    observacao TEXT,
    realizado_em TIMESTAMP,
    proximo_contato_em TIMESTAMP,
    CONSTRAINT fk_contato_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_contato_pp FOREIGN KEY (procedimento_paciente_id) REFERENCES procedimentos_paciente(id),
    CONSTRAINT fk_contato_usuario FOREIGN KEY (realizado_por_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_pacientes_telefone ON pacientes(telefone);
CREATE INDEX idx_pp_data_proximo_contato ON procedimentos_paciente(data_proximo_contato);
CREATE INDEX idx_pp_paciente_id ON procedimentos_paciente(paciente_id);
CREATE INDEX idx_contatos_paciente_id ON contatos(paciente_id);
CREATE INDEX idx_contatos_realizado_em ON contatos(realizado_em);
CREATE INDEX idx_contatos_status ON contatos(status);