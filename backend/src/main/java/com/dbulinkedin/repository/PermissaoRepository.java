package com.dbulinkedin.repository;

import com.dbulinkedin.entity.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {
    List<Permissao> findByUsuarioIdUsuario(Integer idUsuario);
}