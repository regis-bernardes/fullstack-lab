package com.dbulinkedin.service;

import com.dbulinkedin.dto.FuncionalidadeRequestDto;
import com.dbulinkedin.dto.FuncionalidadeResponseDto;
import com.dbulinkedin.entity.Funcionalidade;
import com.dbulinkedin.repository.FuncionalidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionalidadeService {

    private final FuncionalidadeRepository repository;

    @Transactional(readOnly = true)
    public List<FuncionalidadeResponseDto> listarTodas() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public FuncionalidadeResponseDto criar(FuncionalidadeRequestDto dto) {
        Funcionalidade funcionalidade = Funcionalidade.builder()
                .nome(dto.getNome())
                .build();
        return toDto(repository.save(funcionalidade));
    }

    private FuncionalidadeResponseDto toDto(Funcionalidade entity) {
        return FuncionalidadeResponseDto.builder()
                .idFuncionalidade(entity.getIdFuncionalidade())
                .nome(entity.getNome())
                .build();
    }
}