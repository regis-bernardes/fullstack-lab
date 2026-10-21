package com.dbulinkedin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServidorSgbdResponseDto {
    private Integer idServidor;
    private String nomeServidor;
    private String host;
    private Integer porta;
    private String tipo;
    private String username;
    private Boolean ativo;
    private String databaseName;
}