package com.dbulinkedin.dto;

import com.dbulinkedin.enums.TipoExecucao;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class DispararExecucaoRequestDto {
    private String apelido;
    
    @NotNull(message = "O tipo de execução é obrigatório (QUERY, SCRIPT)")
    private TipoExecucao tipoExecucao;

    @NotNull(message = "O ID do servidor não pode estar vazio")
    private Integer idServidor;

    @NotEmpty(message = "O conteúdo SQL não pode estar vazio")
    private String conteudoSql;
}