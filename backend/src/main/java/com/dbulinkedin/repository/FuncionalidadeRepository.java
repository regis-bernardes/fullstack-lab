package com.dbulinkedin.repository;

import com.dbulinkedin.entity.Funcionalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Integer> {
    Optional<Funcionalidade> findByNome(String nome);
}