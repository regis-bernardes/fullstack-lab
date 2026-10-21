package com.dbulinkedin.dto;

import com.dbulinkedin.enums.StatusExecucao;
import com.dbulinkedin.enums.TipoExecucao;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ResultadoExecucaoResponseDto {
    private Integer idResultadoExecucao;
    private Integer idExecucao;
    private Integer idServidor;
    private String nomeServidor;
    private TipoExecucao tipoExecucao;
    private String conteudo;
    private LocalDateTime dtInicio;
    private LocalDateTime dtFim;
    private StatusExecucao status;
    private Integer linhasAfetadas;
    private String mensagemErro;
    private String resultados;
}