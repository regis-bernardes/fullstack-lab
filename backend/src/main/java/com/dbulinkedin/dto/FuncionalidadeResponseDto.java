package com.dbulinkedin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FuncionalidadeResponseDto {
    private Integer idFuncionalidade;
    private String nome;
}