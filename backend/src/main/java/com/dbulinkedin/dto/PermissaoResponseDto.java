package com.dbulinkedin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissaoResponseDto {
    private Integer idPermissao;
    private Integer idUsuario;
    private String loginUsuario;
    private Integer idFuncionalidade;
    private String nomeFuncionalidade;
    private String flagPodeAcessar;
}