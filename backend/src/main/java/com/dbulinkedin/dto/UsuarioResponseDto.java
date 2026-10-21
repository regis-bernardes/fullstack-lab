package com.dbulinkedin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponseDto {
    private Integer idUsuario;
    private String login;
    private String idKeycloak;
}