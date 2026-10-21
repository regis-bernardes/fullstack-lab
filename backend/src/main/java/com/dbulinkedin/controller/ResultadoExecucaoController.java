package com.dbulinkedin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbulinkedin.dto.ResultadoExecucaoResponseDto;
import com.dbulinkedin.service.ResultadoExecucaoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resultados")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RequiredArgsConstructor
public class ResultadoExecucaoController {

    private final ResultadoExecucaoService service;

    @GetMapping("/{idExecucao}/resultado")
    public ResponseEntity<List<ResultadoExecucaoResponseDto>> resultadoExecucao(@PathVariable Integer idExecucao) {
        return ResponseEntity.ok(service.findByIdExecucao(idExecucao));
    }
}