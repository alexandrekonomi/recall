ALTER TABLE contatos
    DROP COLUMN IF EXISTS resultado;

ALTER TABLE contatos
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE';

ALTER TABLE contatos
    ALTER COLUMN realizado_por_id DROP NOT NULL;

ALTER TABLE contatos
    ALTER COLUMN realizado_em DROP NOT NULL;

CREATE INDEX idx_contatos_status ON contatos(status);