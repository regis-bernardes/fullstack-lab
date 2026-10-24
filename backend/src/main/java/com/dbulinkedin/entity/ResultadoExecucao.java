package com.dbulinkedin.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.dbulinkedin.enums.StatusExecucao;
import com.dbulinkedin.enums.TipoExecucao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resultados_execucao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadoExecucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado_execucao")
    private Integer idResultadoExecucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_execucao", nullable = false)
    private Execucao execucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servidor", nullable = false)
    private ServidorSgbd servidor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_execucao", nullable = false, length = 10)
    private TipoExecucao tipoExecucao;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "dt_inicio", nullable = false)
    private LocalDateTime dtInicio;

    @Column(name = "dt_fim")
    private LocalDateTime dtFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusExecucao status;

    @Column(name = "linhas_afetadas")
    private Integer linhasAfetadas;

    @Column(name = "mensagem_erro", columnDefinition = "TEXT")
    private String mensagemErro;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resultados", columnDefinition = "jsonb")
    private String resultados;

    
}