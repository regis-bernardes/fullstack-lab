package com.dbulinkedin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDto {
    @NotBlank(message = "O login é obrigatório")
    private String login;

    @NotBlank(message = "O ID do Keycloak é obrigatório")
    private String idKeycloak;
}