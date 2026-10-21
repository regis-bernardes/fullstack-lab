package com.dbulinkedin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbulinkedin.dto.ExecucaoResponseDto;
import com.dbulinkedin.dto.ResultadoExecucaoResponseDto;
import com.dbulinkedin.entity.Execucao;
import com.dbulinkedin.entity.ResultadoExecucao;
import com.dbulinkedin.repository.ResultadoExecucaoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultadoExecucaoService {

    private final ResultadoExecucaoRepository resultadoExecucaoRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional(readOnly = true)
    public List<ResultadoExecucaoResponseDto> findByIdExecucao(Integer idExecucao) {
    	List<ResultadoExecucao> resultado = resultadoExecucaoRepository.findByExecucaoIdExecucao(idExecucao);
        return resultado.stream()
                .map(this::toResultadoExecucaoResponseDto)
                .collect(Collectors.toList());
    }

    private ResultadoExecucaoResponseDto toResultadoExecucaoResponseDto(ResultadoExecucao entity) {
    	return ResultadoExecucaoResponseDto.builder()
    			.idResultadoExecucao(entity.getIdResultadoExecucao())
    			.idExecucao(entity.getExecucao().getIdExecucao())
    			.idServidor(entity.getServidor().getIdServidor())
    			.tipoExecucao(entity.getTipoExecucao())
    			.conteudo(entity.getConteudo())
    		    .dtInicio(entity.getDtInicio())
    		    .dtFim(entity.getDtFim())
    		    .status(entity.getStatus())
    		    .linhasAfetadas(entity.getLinhasAfetadas())
    		    .mensagemErro(entity.getMensagemErro())
    		    .resultados(entity.getResultados())
    			.build();
    }
}