package com.dbulinkedin.service;

import com.dbulinkedin.dto.ServidorSgbdRequestDto;
import com.dbulinkedin.dto.ServidorSgbdResponseDto;
import com.dbulinkedin.entity.ServidorSgbd;
import com.dbulinkedin.repository.ServidorSgbdRepository;
import com.dbulinkedin.util.CriptografiaService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServidorSgbdService {

    private final ServidorSgbdRepository repository;
    private final CriptografiaService criptografiaService;

    public Page<ServidorSgbd> listarComPaginacao(int page, int size, String sortBy) {
        // Cria o objeto Pageable ordenado por ID (ou outro campo) de forma descendente ou ascendente
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return repository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public List<ServidorSgbdResponseDto> listarTodos() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServidorSgbdResponseDto buscarPorId(Integer id) {
        ServidorSgbd servidor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servidor SGBD não encontrado com o ID: " + id));
        return toResponseDto(servidor);
    }

    @Transactional
    public ServidorSgbdResponseDto criar(ServidorSgbdRequestDto dto) {
        String senhaCifrada = criptografiaService.criptografar(dto.getSenhaCriptografada());

        ServidorSgbd servidor = ServidorSgbd.builder()
                .nomeServidor(dto.getNomeServidor())
                .host(dto.getHost())
                .porta(dto.getPorta())
                .tipo(dto.getTipo())
                .username(dto.getUsername())
                .senhaCriptografada(senhaCifrada)
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .databaseName(dto.getDatabaseName())
                .build();

        ServidorSgbd salvo = repository.save(servidor);
        return toResponseDto(salvo);
    }

    @Transactional
    public ServidorSgbdResponseDto atualizar(Integer id, ServidorSgbdRequestDto dto) {
        ServidorSgbd servidor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servidor SGBD não encontrado com o ID: " + id));

        servidor.setNomeServidor(dto.getNomeServidor());
        servidor.setHost(dto.getHost());
        servidor.setPorta(dto.getPorta());
        servidor.setTipo(dto.getTipo());
        servidor.setUsername(dto.getUsername());
        servidor.setDatabaseName(dto.getDatabaseName());

        if (dto.getSenhaCriptografada() != null && !dto.getSenhaCriptografada().isBlank()) {
            servidor.setSenhaCriptografada(criptografiaService.criptografar(dto.getSenhaCriptografada()));
        }

        if (dto.getAtivo() != null) {
            servidor.setAtivo(dto.getAtivo());
        }

        ServidorSgbd atualizado = repository.save(servidor);
        return toResponseDto(atualizado);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Servidor SGBD não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    private ServidorSgbdResponseDto toResponseDto(ServidorSgbd entity) {
        return ServidorSgbdResponseDto.builder()
                .idServidor(entity.getIdServidor())
                .nomeServidor(entity.getNomeServidor())
                .host(entity.getHost())
                .porta(entity.getPorta())
                .tipo(entity.getTipo())
                .username(entity.getUsername())
                .ativo(entity.getAtivo())
                .databaseName(entity.getDatabaseName())
                .build();
    }
}