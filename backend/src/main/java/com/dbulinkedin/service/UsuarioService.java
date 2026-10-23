package com.dbulinkedin.service;

import com.dbulinkedin.dto.UsuarioRequestDto;
import com.dbulinkedin.dto.UsuarioResponseDto;
import com.dbulinkedin.entity.Usuario;
import com.dbulinkedin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar operações relacionadas a usuários.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    /**
     * Lista todos os usuários disponíveis no sistema.
     *
     * @return Lista de DTOs representando os usuários.
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarTodos() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id ID do usuário a ser buscado.
     * @return DTO representando o usuário encontrado.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorId(Integer id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        return toDto(usuario);
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param dto DTO contendo as informações do usuário a ser criado.
     * @return DTO representando o usuário criado.
     */
    @Transactional
    public UsuarioResponseDto criar(UsuarioRequestDto dto) {
        Usuario usuario = Usuario.builder()
                .login(dto.getLogin())
                .idKeycloak(dto.getIdKeycloak())
                .build();
        return toDto(repository.save(usuario));
    }

    /**
     * Elimina um usuário pelo seu ID.
     *
     * @param id ID do usuário a ser eliminado.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    @Transactional
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Converte uma entidade Usuario para um DTO UsuarioResponseDto.
     *
     * @param entity Entidade Usuario a ser convertida.
     * @return DTO UsuarioResponseDto.
     */
    private UsuarioResponseDto toDto(Usuario entity) {
        return UsuarioResponseDto.builder()
                .idUsuario(entity.getIdUsuario())
                .login(entity.getLogin())
                .idKeycloak(entity.getIdKeycloak())
                .build();
    }
}
