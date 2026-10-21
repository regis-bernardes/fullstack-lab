package com.dbulinkedin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permissao")
    private Integer idPermissao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_funcionalidade", nullable = false)
    private Funcionalidade funcionalidade;

    @Column(name = "flag_pode_acessar", nullable = false, length = 1)
    private String flagPodeAcessar; // 'S' ou 'N'
}