ALTER TABLE usuarios ADD COLUMN token_convite VARCHAR(255);
ALTER TABLE usuarios ADD COLUMN convite_expira_em TIMESTAMP;
ALTER TABLE usuarios ADD COLUMN convidado_por_id BIGINT;
ALTER TABLE usuarios ALTER COLUMN senha DROP NOT NULL;

ALTER TABLE usuarios ADD CONSTRAINT fk_usuario_convidado_por
    FOREIGN KEY (convidado_por_id) REFERENCES usuarios(id);

CREATE INDEX idx_usuarios_token_convite ON usuarios(token_convite);