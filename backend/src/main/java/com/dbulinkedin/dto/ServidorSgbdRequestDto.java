package com.dbulinkedin.dto;

import com.dbulinkedin.enums.TipoSgbd;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServidorSgbdRequestDto {

    @NotBlank(message = "O nome do servidor é obrigatório")
    private String nomeServidor;

    @NotBlank(message = "O host é obrigatório")
    private String host;

    @NotNull(message = "A porta é obrigatória")
    private Integer porta;

    @NotNull(message = "O tipo de servidor é obrigatório (POSTGRES, ORACLE, MySql)")
    @Enumerated(EnumType.STRING)
    private TipoSgbd tipo;

    @NotBlank(message = "O username é obrigatório")
    private String username;

    @NotBlank(message = "A senha é obrigatória")
    private String senhaCriptografada;

    private Boolean ativo;

    @NotBlank(message = "O Nome do Database é obrigatório")
    private String databaseName;

}
