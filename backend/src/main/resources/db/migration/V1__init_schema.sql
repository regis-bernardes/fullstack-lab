-- Criação da tabela de Usuários
CREATE TABLE usuarios (
    id_usuario INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login VARCHAR(20) NOT NULL UNIQUE,
    id_keycloak VARCHAR(100) NOT NULL
);

-- Criação da tabela de Funcionalidades
CREATE TABLE funcionalidades (
    id_funcionalidade INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

-- Criação da tabela de Permissões
CREATE TABLE permissoes (
    id_permissao INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    id_funcionalidade INTEGER NOT NULL,
    flag_pode_acessar VARCHAR(1) NOT NULL,
    
    CONSTRAINT fk_perm_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario),
        
    CONSTRAINT fk_perm_funcionalidade
        FOREIGN KEY (id_funcionalidade)
        REFERENCES funcionalidades(id_funcionalidade)
);

-- Criação da tabela de Servidores SGBD
CREATE TABLE servidores_sgbd (
    id_servidor INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_servidor VARCHAR(40) NOT NULL,
    host VARCHAR(100) NOT NULL,
    porta INTEGER NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    username VARCHAR(50) NOT NULL,
    senha_criptografada TEXT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Criação da tabela de Execuções principais
CREATE TABLE execucoes (
    id_execucao INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    apelido VARCHAR(30),
    usuario_executor VARCHAR(50) NOT NULL,
    dt_inicio TIMESTAMP NOT NULL,
    dt_fim TIMESTAMP,
    status INTEGER NOT NULL
);

-- Criação da tabela de Resultados de Execução (Itens paralelos)
CREATE TABLE resultados_execucao (
    id_resultado_execucao INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_execucao INTEGER NOT NULL,
    id_servidor INTEGER NOT NULL,
    tipo_execucao VARCHAR(10) NOT NULL,
    conteudo TEXT NOT NULL,
    dt_inicio TIMESTAMP NOT NULL,
    dt_fim TIMESTAMP,
    status INTEGER NOT NULL,
    linhas_afetadas INTEGER,
    mensagem_erro TEXT,

    CONSTRAINT fk_item_execucao
        FOREIGN KEY (id_execucao)
        REFERENCES execucoes(id_execucao),
        
    CONSTRAINT fk_item_servidor
        FOREIGN KEY (id_servidor)
        REFERENCES servidores_sgbd(id_servidor)
);