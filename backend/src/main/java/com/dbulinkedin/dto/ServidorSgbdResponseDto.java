package com.dbulinkedin.dto;

import com.dbulinkedin.enums.TipoSgbd;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServidorSgbdResponseDto {
    private Integer idServidor;
    private String nomeServidor;
    private String host;
    private Integer porta;
    private TipoSgbd tipo;
    private String username;
    private Boolean ativo;
    private String databaseName;
}
