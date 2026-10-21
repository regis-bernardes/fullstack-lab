package com.dbulinkedin.service;

import com.dbulinkedin.dto.PermissaoRequestDto;
import com.dbulinkedin.dto.PermissaoResponseDto;
import com.dbulinkedin.entity.Funcionalidade;
import com.dbulinkedin.entity.Permissao;
import com.dbulinkedin.entity.Usuario;
import com.dbulinkedin.repository.FuncionalidadeRepository;
import com.dbulinkedin.repository.PermissaoRepository;
import com.dbulinkedin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FuncionalidadeRepository funcionalidadeRepository;

    @Transactional(readOnly = true)
    public List<PermissaoResponseDto> listarPorUsuario(Integer idUsuario) {
        return permissaoRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PermissaoResponseDto atribuir(PermissaoRequestDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Funcionalidade funcionalidade = funcionalidadeRepository.findById(dto.getIdFuncionalidade())
                .orElseThrow(() -> new RuntimeException("Funcionalidade não encontrada"));

        Permissao permissao = Permissao.builder()
                .usuario(usuario)
                .funcionalidade(funcionalidade)
                .flagPodeAcessar(dto.getFlagPodeAcessar())
                .build();

        return toDto(permissaoRepository.save(permissao));
    }

    private PermissaoResponseDto toDto(Permissao entity) {
        return PermissaoResponseDto.builder()
                .idPermissao(entity.getIdPermissao())
                .idUsuario(entity.getUsuario().getIdUsuario())
                .loginUsuario(entity.getUsuario().getLogin())
                .idFuncionalidade(entity.getFuncionalidade().getIdFuncionalidade())
                .nomeFuncionalidade(entity.getFuncionalidade().getNome())
                .flagPodeAcessar(entity.getFlagPodeAcessar())
                .build();
    }
}