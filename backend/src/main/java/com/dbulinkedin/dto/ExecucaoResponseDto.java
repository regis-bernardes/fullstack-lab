package com.dbulinkedin.dto;

import com.dbulinkedin.entity.ServidorSgbd;
import com.dbulinkedin.enums.StatusExecucao;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ExecucaoResponseDto {
    private Integer idExecucao;
    private String apelido;
    private String usuarioExecutor;
    private LocalDateTime dtInicio;
    private LocalDateTime dtFim;
    private StatusExecucao status;
    private String tipoExecucao;
    private String conteudo;
    private ServidorSgbd servidor;
    private List<ResultadoExecucaoResponseDto> resultados;
}