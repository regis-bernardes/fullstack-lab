package com.dbulinkedin.controller;

import com.dbulinkedin.dto.PermissaoRequestDto;
import com.dbulinkedin.dto.PermissaoResponseDto;
import com.dbulinkedin.service.PermissaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissoes")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RequiredArgsConstructor
public class PermissaoController {

    private final PermissaoService service;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PermissaoResponseDto>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }

    @PostMapping
    public ResponseEntity<PermissaoResponseDto> atribuir(@Valid @RequestBody PermissaoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.atribuir(dto));
    }
}