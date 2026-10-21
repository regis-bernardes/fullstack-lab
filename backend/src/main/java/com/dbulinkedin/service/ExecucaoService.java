package com.dbulinkedin.service;

import com.dbulinkedin.dto.DispararExecucaoRequestDto;
import com.dbulinkedin.dto.ExecucaoResponseDto;
import com.dbulinkedin.dto.ResultadoExecucaoResponseDto;
import com.dbulinkedin.entity.Execucao;
import com.dbulinkedin.entity.ResultadoExecucao;
import com.dbulinkedin.enums.StatusExecucao;
import com.dbulinkedin.executor.SqlExecutorEngine;
import com.dbulinkedin.repository.ExecucaoRepository;
import com.dbulinkedin.repository.ResultadoExecucaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExecucaoService {

    private final SqlExecutorEngine sqlExecutorEngine;
    private final ExecucaoRepository execucaoRepository;
    private final ResultadoExecucaoRepository resultadoExecucaoRepository;

    @Transactional
    public ExecucaoResponseDto disparar(String usuarioExecutor, DispararExecucaoRequestDto dto) {
        Execucao execucao = sqlExecutorEngine.dispararExecucao(
                usuarioExecutor,
                dto.getApelido(),
                dto.getIdServidor(),
                dto.getTipoExecucao(),
                dto.getConteudoSql()
        );
        return buscarDetalhes(execucao.getIdExecucao());
    }

    @Transactional(readOnly = true)
    public List<ExecucaoResponseDto> listarHistorico() {
        return execucaoRepository.findAll().stream()
                .map(this::toExecucaoResponseDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ExecucaoResponseDto buscarDetalhes(Integer idExecucao) {
        Execucao execucao = execucaoRepository.findById(idExecucao)
                .orElseThrow(() -> new RuntimeException("Execução não encontrada: " + idExecucao));
        return toExecucaoResponseDto(execucao);
    }

    @Transactional
    public void cancelarExecucao(Integer idExecucao) {
        Execucao execucao = execucaoRepository.findById(idExecucao)
                .orElseThrow(() -> new RuntimeException("Execução não encontrada: " + idExecucao));

        if (execucao.getStatus() == StatusExecucao.CONCLUIDO_COM_SUCESSO || 
            execucao.getStatus() == StatusExecucao.CONCLUIDO_COM_ERRO) {
            throw new IllegalStateException("Não é possível cancelar uma execução já concluída.");
        }

        execucao.setStatus(StatusExecucao.CANCELADO);
        execucao.setDtFim(LocalDateTime.now());
        execucaoRepository.save(execucao);

        List<ResultadoExecucao> resultados = resultadoExecucaoRepository.findByExecucaoIdExecucao(idExecucao);
        for (ResultadoExecucao resultado : resultados) {
            if (resultado.getStatus() == StatusExecucao.EM_EXECUCAO) {
                resultado.setStatus(StatusExecucao.CANCELADO);
                resultado.setDtFim(LocalDateTime.now());
                resultadoExecucaoRepository.save(resultado);
            }
        }
    }

    private ExecucaoResponseDto toExecucaoResponseDto(Execucao entity) {
        List<ResultadoExecucao> resultados = resultadoExecucaoRepository.findByExecucaoIdExecucao(entity.getIdExecucao());
        
        List<ResultadoExecucaoResponseDto> resultadoDtos = resultados.stream().map(r -> 
            ResultadoExecucaoResponseDto.builder()
                    .idResultadoExecucao(r.getIdResultadoExecucao())
                    .idServidor(r.getServidor().getIdServidor())
                    .nomeServidor(r.getServidor().getNomeServidor())
                    .tipoExecucao(r.getTipoExecucao())
                    .conteudo(r.getConteudo())
                    .dtInicio(r.getDtInicio())
                    .dtFim(r.getDtFim())
                    .status(r.getStatus())
                    .linhasAfetadas(r.getLinhasAfetadas())
                    .mensagemErro(r.getMensagemErro())
                    .build()
        ).collect(Collectors.toList());

        return ExecucaoResponseDto.builder()
                .idExecucao(entity.getIdExecucao())
                .apelido(entity.getApelido())
                .usuarioExecutor(entity.getUsuarioExecutor())
                .dtInicio(entity.getDtInicio())
                .dtFim(entity.getDtFim())
                .status(entity.getStatus())
                .tipoExecucao(entity.getTipoExecucao())
                .conteudo(entity.getConteudo())
                .resultados(resultadoDtos)
                .servidor(entity.getServidor())
                .build();
    }
}