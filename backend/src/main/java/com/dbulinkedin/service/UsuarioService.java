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

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarTodos() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorId(Integer id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        return toDto(usuario);
    }

    @Transactional
    public UsuarioResponseDto criar(UsuarioRequestDto dto) {
        Usuario usuario = Usuario.builder()
                .login(dto.getLogin())
                .idKeycloak(dto.getIdKeycloak())
                .build();
        return toDto(repository.save(usuario));
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    private UsuarioResponseDto toDto(Usuario entity) {
        return UsuarioResponseDto.builder()
                .idUsuario(entity.getIdUsuario())
                .login(entity.getLogin())
                .idKeycloak(entity.getIdKeycloak())
                .build();
    }
}