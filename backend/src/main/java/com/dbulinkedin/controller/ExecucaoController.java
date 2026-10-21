package com.dbulinkedin.controller;

import com.dbulinkedin.dto.DispararExecucaoRequestDto;
import com.dbulinkedin.dto.ExecucaoResponseDto;
import com.dbulinkedin.service.ExecucaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/execucoes")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RequiredArgsConstructor
public class ExecucaoController {

    private final ExecucaoService service;

    @PostMapping
    public ResponseEntity<ExecucaoResponseDto> disparar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody DispararExecucaoRequestDto dto) {
        String usuarioExecutor = jwt != null ? jwt.getSubject() : "sistema";
        ExecucaoResponseDto response = service.disparar(usuarioExecutor, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExecucaoResponseDto>> listarHistorico() {
        return ResponseEntity.ok(service.listarHistorico());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExecucaoResponseDto> buscarDetalhes(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarDetalhes(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        service.cancelarExecucao(id);
        return ResponseEntity.noContent().build();
    }
}