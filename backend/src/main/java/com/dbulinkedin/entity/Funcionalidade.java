package com.dbulinkedin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "funcionalidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionalidade")
    private Integer idFuncionalidade;

    @Column(nullable = false, unique = true, length = 50)
    private String nome; // Ex: GERENCIAR_SERVIDORES, EXECUTAR_SQL, etc.
}