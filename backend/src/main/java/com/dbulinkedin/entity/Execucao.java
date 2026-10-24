package com.dbulinkedin.entity;

import com.dbulinkedin.enums.StatusExecucao;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "execucoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Execucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_execucao")
    private Integer idExecucao;

    @Column(length = 30)
    private String apelido;

    @Column(name = "usuario_executor", nullable = false, length = 50)
    private String usuarioExecutor;

    @Column(name = "dt_inicio", nullable = false)
    private LocalDateTime dtInicio;

    @Column(name = "dt_fim")
    private LocalDateTime dtFim;

    @Column(name="tipo_execucao", nullable = false)
    private String tipoExecucao;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;
    
    @Enumerated(EnumType.STRING) // Mapeia para o inteiro da tabela (1, 2, 3, 4)
    @Column(nullable = false)
    private StatusExecucao status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servidor", nullable = false)
    private ServidorSgbd servidor;
    
    @OneToMany(mappedBy = "execucao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultadoExecucao> resultados;
}