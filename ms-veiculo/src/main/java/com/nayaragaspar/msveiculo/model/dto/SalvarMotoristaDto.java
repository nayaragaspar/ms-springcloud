package com.nayaragaspar.msveiculo.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SalvarMotoristaDto(@NotBlank String nome, @NotBlank String documento,
                @Email(message = "Informe um email válido!") String email,
                String telefone) {
}
