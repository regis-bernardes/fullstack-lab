package com.dbulinkedin.controller;

import com.dbulinkedin.dto.FuncionalidadeRequestDto;
import com.dbulinkedin.dto.FuncionalidadeResponseDto;
import com.dbulinkedin.service.FuncionalidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionalidades")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RequiredArgsConstructor
public class FuncionalidadeController {

    private final FuncionalidadeService service;

    @GetMapping
    public ResponseEntity<List<FuncionalidadeResponseDto>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @PostMapping
    public ResponseEntity<FuncionalidadeResponseDto> criar(@Valid @RequestBody FuncionalidadeRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }
}