package com.dbulinkedin.executor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.dbulinkedin.entity.Execucao;
import com.dbulinkedin.entity.ResultadoExecucao;
import com.dbulinkedin.entity.ServidorSgbd;
import com.dbulinkedin.enums.StatusExecucao;
import com.dbulinkedin.enums.TipoExecucao;
import com.dbulinkedin.repository.ExecucaoRepository;
import com.dbulinkedin.repository.ResultadoExecucaoRepository;
import com.dbulinkedin.repository.ServidorSgbdRepository;
import com.dbulinkedin.util.CriptografiaService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqlExecutorEngine {

    private final ExecucaoRepository execucaoRepository;
    private final ResultadoExecucaoRepository resultadoExecucaoRepository;
    private final ServidorSgbdRepository servidorSgbdRepository;
    private final CriptografiaService criptografiaService;
    private final ObjectMapper objectMapper; // Injetado para serializar os dados em JSON
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Execucao dispararExecucao(String usuarioExecutor, String apelido, Integer idServidor, TipoExecucao tipoExecucao, String conteudoSql) {
    	ServidorSgbd servidorRef = entityManager.getReference(ServidorSgbd.class, idServidor);
    	
    	// 1. Criar e persistir a Execução principal
        Execucao execucao = Execucao.builder()
                .apelido(apelido)
                .usuarioExecutor(usuarioExecutor)
                .dtInicio(LocalDateTime.now())
                .status(StatusExecucao.EM_EXECUCAO)
                .tipoExecucao(tipoExecucao.toString())
                .conteudo(conteudoSql)
                .servidor(servidorRef)
                .build();
        
        execucao = execucaoRepository.save(execucao);

        // 2. Preparar os itens de resultado para cada servidor e conteúdo SQL
        List<ResultadoExecucao> resultados = new ArrayList<>();

        ServidorSgbd servidor = servidorSgbdRepository.findById(idServidor)
            .orElseThrow(() -> new RuntimeException("Servidor não encontrado: " + idServidor));

        ResultadoExecucao resultado = ResultadoExecucao.builder()
                .execucao(execucao)
                .servidor(servidor)
                .tipoExecucao(tipoExecucao)
                .conteudo(conteudoSql)
                .dtInicio(LocalDateTime.now())
                .status(StatusExecucao.EM_EXECUCAO)
                .build();
        resultados.add(resultado);
   
        resultadoExecucaoRepository.saveAll(resultados);

        // 3. Executar em paralelo utilizando Java Virtual Threads
        Execucao finalExecucao = execucao;
        Thread.ofVirtual().start(() -> processarEmParalelo(finalExecucao, resultados));

        return execucao;
    }

    private void processarEmParalelo(Execucao execucao, List<ResultadoExecucao> resultados) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();

            for (ResultadoExecucao resultado : resultados) {
                futures.add(executor.submit(() -> executarItem(resultado)));
            }

            // Aguardar a conclusão de todas as virtual threads
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    log.error("Erro ao aguardar thread de execução: {}", e.getMessage());
                }
            }

            // Atualizar status final da Execução principal
            atualizarStatusGeral(execucao.getIdExecucao());

        } catch (Exception e) {
            log.error("Erro no bloco de execução virtual: {}", e.getMessage());
        }
    }

    private void executarItem(ResultadoExecucao resultado) {
        ServidorSgbd servidor = resultado.getServidor();
        String senhaDescriptografada = criptografiaService.descriptografar(servidor.getSenhaCriptografada());
        
        // Montar string de conexão JDBC baseada no tipo do servidor (Ex: PostgreSQL)
        String urlJdbc = "";
        if ("POSTGRES".equalsIgnoreCase(servidor.getTipo())) {
            urlJdbc = String.format("jdbc:postgresql://%s:%d/%s", servidor.getHost(), servidor.getPorta(), servidor.getDatabaseName());
        }

        resultado.setDtInicio(LocalDateTime.now());
        
        try (Connection conn = DriverManager.getConnection(urlJdbc, servidor.getUsername(), senhaDescriptografada);
             Statement stmt = conn.createStatement()) {

            boolean isResultSet = stmt.execute(resultado.getConteudo());
            
            Map<String, Object> dadosResultado = new HashMap<>();

            if (isResultSet) {
                // É uma query de consulta (SELECT): Capturar colunas e linhas
                try (ResultSet rs = stmt.getResultSet()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    List<String> colunas = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        colunas.add(metaData.getColumnLabel(i));
                    }

                    List<Map<String, Object>> linhas = new ArrayList<>();
                    int totalLinhas = 0;
                    while (rs.next()) {
                        Map<String, Object> linha = new LinkedHashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            linha.put(colunas.get(i - 1), rs.getObject(i));
                        }
                        linhas.add(linha);
                        totalLinhas++;
                    }

                    dadosResultado.put("colunas", colunas);
                    dadosResultado.put("linhas", linhas);
                    resultado.setLinhasAfetadas(totalLinhas);
                }
            } else {
                // É um comando de atualização/inserção (INSERT, UPDATE, DELETE, etc.)
                int linhasAfetadas = stmt.getUpdateCount();
                dadosResultado.put("linhasAfetadas", linhasAfetadas);
                resultado.setLinhasAfetadas(linhasAfetadas);
            }

           	resultado.setResultados(objectMapper.writeValueAsString(dadosResultado));
            
            resultado.setStatus(StatusExecucao.CONCLUIDO_COM_SUCESSO);
        } catch (SQLException e) {
        	if (e instanceof SQLException) 
        		log.error("Erro ao executar SQL no servidor {}: {}", servidor.getNomeServidor(), e.getMessage());
        	else
        		log.error("Erro ao criar jbonb: {}", e.getMessage());
        	
        	resultado.setStatus(StatusExecucao.CONCLUIDO_COM_ERRO);
            resultado.setMensagemErro(e.getMessage());
        } finally {
            resultado.setDtFim(LocalDateTime.now());
            resultadoExecucaoRepository.save(resultado);
        }
    }

    private void atualizarStatusGeral(Integer idExecucao) {
        Execucao execucao = execucaoRepository.findById(idExecucao).orElse(null);
        if (execucao == null) return;

        List<ResultadoExecucao> itens = resultadoExecucaoRepository.findByExecucaoIdExecucao(idExecucao);
        
        boolean temErro = itens.stream().anyMatch(i -> i.getStatus() == StatusExecucao.CONCLUIDO_COM_ERRO);
        boolean cancelado = itens.stream().allMatch(i -> i.getStatus() == StatusExecucao.CANCELADO);

        if (cancelado) {
            execucao.setStatus(StatusExecucao.CANCELADO);
        } else if (temErro) {
            execucao.setStatus(StatusExecucao.CONCLUIDO_COM_ERRO);
        } else {
            execucao.setStatus(StatusExecucao.CONCLUIDO_COM_SUCESSO);
        }

        execucao.setDtFim(LocalDateTime.now());
        execucaoRepository.save(execucao);
    }
}