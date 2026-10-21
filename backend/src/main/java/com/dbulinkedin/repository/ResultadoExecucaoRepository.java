package com.dbulinkedin.repository;

import com.dbulinkedin.entity.ResultadoExecucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultadoExecucaoRepository extends JpaRepository<ResultadoExecucao, Integer> {
    List<ResultadoExecucao> findByExecucaoIdExecucao(Integer idExecucao);
}