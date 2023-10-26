package com.nayaragaspar.msveiculo.model.dto;

import com.nayaragaspar.msveiculo.model.Motorista;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SalvarMotoristaDto(@NotBlank String nome, @NotBlank String documento,
        @Email(message = "Informe um email válido!") String email,
        String telefone) {

    public Motorista toModel() {
        return new Motorista(null, nome, documento, email, telefone);
    }
}
