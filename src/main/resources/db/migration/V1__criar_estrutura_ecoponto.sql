-- Estrutura inicial do banco EcoPonto Inteligente de Maringá.
-- A hierarquia Usuario/Cidadao/Administrador/Parceiro utiliza SINGLE_TABLE.

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    tipo_usuario VARCHAR(20) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    saldo_pontos INTEGER NOT NULL DEFAULT 0,
    cnpj VARCHAR(30),
    CONSTRAINT usuarios_saldo_ck CHECK (saldo_pontos >= 0),
    CONSTRAINT usuarios_tipo_ck CHECK (tipo_usuario IN ('CIDADAO', 'ADMINISTRADOR', 'PARCEIRO'))
);

CREATE TABLE ecopontos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    latitude NUMERIC(10,7),
    longitude NUMERIC(10,7)
);

CREATE TABLE compartimentos (
    id BIGSERIAL PRIMARY KEY,
    ecoponto_id BIGINT NOT NULL,
    tipo_residuo VARCHAR(30) NOT NULL,
    capacidade_maxima NUMERIC(10,3) NOT NULL,
    nivel_atual NUMERIC(10,3) NOT NULL DEFAULT 0,
    alerta_percentual NUMERIC(5,2) NOT NULL DEFAULT 80.00,
    status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL',
    CONSTRAINT compartimentos_ecoponto_fk FOREIGN KEY (ecoponto_id)
        REFERENCES ecopontos(id) ON DELETE CASCADE,
    CONSTRAINT compartimentos_tipo_ck CHECK (tipo_residuo IN ('RECICLAVEL', 'ELETRONICO', 'ORGANICO')),
    CONSTRAINT compartimentos_capacidade_ck CHECK (capacidade_maxima > 0),
    CONSTRAINT compartimentos_nivel_ck CHECK (nivel_atual >= 0 AND nivel_atual <= capacidade_maxima),
    CONSTRAINT compartimentos_alerta_ck CHECK (alerta_percentual > 0 AND alerta_percentual <= 100)
);

CREATE TABLE descartes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    compartimento_id BIGINT NOT NULL,
    tipo_residuo VARCHAR(30) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    peso_kg NUMERIC(10,3) NOT NULL,
    pontos_gerados INTEGER NOT NULL,
    CONSTRAINT descartes_usuario_fk FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT descartes_compartimento_fk FOREIGN KEY (compartimento_id) REFERENCES compartimentos(id),
    CONSTRAINT descartes_peso_ck CHECK (peso_kg > 0),
    CONSTRAINT descartes_pontos_ck CHECK (pontos_gerados >= 0),
    CONSTRAINT descartes_tipo_ck CHECK (tipo_residuo IN ('RECICLAVEL', 'ELETRONICO', 'ORGANICO'))
);

CREATE TABLE recompensas (
    id BIGSERIAL PRIMARY KEY,
    parceiro_id BIGINT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    custo_pontos INTEGER NOT NULL,
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT recompensas_parceiro_fk FOREIGN KEY (parceiro_id) REFERENCES usuarios(id),
    CONSTRAINT recompensas_custo_ck CHECK (custo_pontos > 0)
);

CREATE TABLE resgates (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    recompensa_id BIGINT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    pontos_usados INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT resgates_usuario_fk FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT resgates_recompensa_fk FOREIGN KEY (recompensa_id) REFERENCES recompensas(id),
    CONSTRAINT resgates_pontos_ck CHECK (pontos_usados > 0),
    CONSTRAINT resgates_status_ck CHECK (status IN ('CONFIRMADO', 'CANCELADO'))
);

CREATE INDEX compartimentos_ecoponto_ix ON compartimentos(ecoponto_id);
CREATE INDEX descartes_usuario_data_ix ON descartes(usuario_id, data_hora DESC);
CREATE INDEX descartes_compartimento_ix ON descartes(compartimento_id);
CREATE INDEX descartes_tipo_data_ix ON descartes(tipo_residuo, data_hora DESC);
CREATE INDEX recompensas_parceiro_ix ON recompensas(parceiro_id);
CREATE INDEX resgates_usuario_data_ix ON resgates(usuario_id, data_hora DESC);
