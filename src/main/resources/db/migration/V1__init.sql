CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL
);

CREATE TABLE procedimentos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    intervalo_retorno_dias INTEGER NOT NULL,
    template_mensagem TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_por_id UUID NOT NULL,
    CONSTRAINT fk_procedimento_usuario FOREIGN KEY (criado_por_id) REFERENCES usuarios(id)
);

CREATE TABLE pacientes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL UNIQUE,
    data_nascimento DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP
);

CREATE TABLE paciente_tags (
    paciente_id UUID NOT NULL,
    tag VARCHAR(20) NOT NULL,
    CONSTRAINT fk_tag_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    PRIMARY KEY (paciente_id, tag)
);

CREATE TABLE procedimentos_paciente (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    paciente_id UUID NOT NULL,
    procedimento_id UUID NOT NULL,
    data_realizacao DATE NOT NULL,
    data_proximo_contato DATE NOT NULL,
    registrado_por_id UUID,
    criado_em TIMESTAMP NOT NULL,
    CONSTRAINT fk_pp_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_pp_procedimento FOREIGN KEY (procedimento_id) REFERENCES procedimentos(id)
);

CREATE TABLE contatos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    paciente_id UUID NOT NULL,
    procedimento_paciente_id UUID NOT NULL,
    realizado_por_id UUID NOT NULL,
    resultado VARCHAR(20),
    mensagem_enviada TEXT,
    observacao TEXT,
    realizado_em TIMESTAMP NOT NULL,
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