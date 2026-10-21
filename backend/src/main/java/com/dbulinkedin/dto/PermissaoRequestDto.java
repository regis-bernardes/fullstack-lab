package com.dbulinkedin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissaoRequestDto {
    @NotNull(message = "O ID do usuário é obrigatório")
    private Integer idUsuario;

    @NotNull(message = "O ID da funcionalidade é obrigatório")
    private Integer idFuncionalidade;

    @NotBlank(message = "A flag de acesso (S/N) é obrigatória")
    private String flagPodeAcessar;
}