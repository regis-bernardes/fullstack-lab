package com.dbulinkedin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FuncionalidadeRequestDto {
    @NotBlank(message = "O nome da funcionalidade é obrigatório")
    private String nome;
}