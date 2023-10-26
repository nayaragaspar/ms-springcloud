package com.nayaragaspar.msveiculo.model.dto;

import java.util.UUID;

import com.nayaragaspar.msveiculo.model.Motorista;
import com.nayaragaspar.msveiculo.model.Veiculo;

import jakarta.validation.constraints.NotNull;

public record SalvarVeiculoDto(@NotNull String placa, String modelo, String marca, String epcTag,
        @NotNull String memoryTag,
        @NotNull UUID idMotorista) {

    public Veiculo toModel(Motorista motorista) {
        return new Veiculo(null, placa, modelo, marca, epcTag, memoryTag, motorista);
    }
}
