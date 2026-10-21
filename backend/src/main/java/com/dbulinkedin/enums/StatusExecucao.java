package com.dbulinkedin.enums;

public enum StatusExecucao {
    EM_EXECUCAO(1),
    CONCLUIDO_COM_ERRO(2),
    CONCLUIDO_COM_SUCESSO(3),
    CANCELADO(4);

    private final int codigo;

    StatusExecucao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}