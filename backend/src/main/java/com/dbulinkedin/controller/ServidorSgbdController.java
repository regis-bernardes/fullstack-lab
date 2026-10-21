package com.dbulinkedin.controller;

import com.dbulinkedin.dto.ServidorSgbdRequestDto;
import com.dbulinkedin.dto.ServidorSgbdResponseDto;
import com.dbulinkedin.entity.ServidorSgbd;
import com.dbulinkedin.service.ServidorSgbdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servidores")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RequiredArgsConstructor
public class ServidorSgbdController {

    private final ServidorSgbdService service;

    //@GetMapping
    //public ResponseEntity<List<ServidorSgbdResponseDto>> listarTodos() {
    //    return ResponseEntity.ok(service.listarTodos());
    //    
    //}

    @GetMapping
    public ResponseEntity<Page<ServidorSgbd>> listar(
            @RequestParam(defaultValue = "0") int page,      // Página atual (começa em 0 no Spring)
            @RequestParam(defaultValue = "5") int size,      // Itens por página
            @RequestParam(defaultValue = "idServidor") String sortBy // Campo para ordenação
    ) {
        Page<ServidorSgbd> paginaServidores = service.listarComPaginacao(page, size, sortBy);
        return ResponseEntity.ok(paginaServidores);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ServidorSgbdResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ServidorSgbdResponseDto> criar(@Valid @RequestBody ServidorSgbdRequestDto dto) {
        ServidorSgbdResponseDto criado = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorSgbdResponseDto> atualizar(@PathVariable Integer id, @Valid @RequestBody ServidorSgbdRequestDto dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}