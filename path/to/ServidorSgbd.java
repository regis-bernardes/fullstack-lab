package com.dbulinkedin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "servidores_sgbd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServidorSgbd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servidor")
    private Integer idServidor;

    @Column(name = "nome_servidor", nullable = false, length = 40)
    private String nomeServidor;

    @Column(nullable = false, length = 100)
    private String host;

    @Column(nullable = false)
    private Integer porta;

    @Column(nullable = false, length = 20)
    private String tipo; // POSTGRES, ORACLE, SQLSERVER

    @Column(nullable = false, length = 50)
    private String username;

    @Column(name = "senha_criptografada", nullable = false, columnDefinition = "TEXT")
    private String senhaCriptografada;

    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "database_name", nullable = false, length = 30)
    private String databaseName;

    
    // Alteração: Corrigir variável e atributo
    private String TipoSgbd;
}
