package com.dbulinkedin.repository;

import com.dbulinkedin.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findByIdKeycloak(String idKeycloak);
}