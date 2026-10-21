package com.dbulinkedin.repository;

import com.dbulinkedin.entity.Execucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecucaoRepository extends JpaRepository<Execucao, Integer> {
}