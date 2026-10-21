package com.dbulinkedin.service;

import com.dbulinkedin.dto.ExecucaoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notificarAtualizacao(ExecucaoResponseDto execucaoDto) {
        // Envia os dados atualizados da execução para o tópico específico do ID
        messagingTemplate.convertAndSend("/topic/execucoes/" + execucaoDto.getIdExecucao(), execucaoDto);
        // Também envia para um tópico geral de histórico/dashboard se necessário
        messagingTemplate.convertAndSend("/topic/execucoes", execucaoDto);
    }
}