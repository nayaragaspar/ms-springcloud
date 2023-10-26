package com.nayaragaspar.msveiculo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nayaragaspar.msveiculo.exception.NotFoundException;
import com.nayaragaspar.msveiculo.model.Motorista;
import com.nayaragaspar.msveiculo.model.dto.SalvarMotoristaDto;
import com.nayaragaspar.msveiculo.repository.MotoristaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MotoristaService {
    private final MotoristaRepository motoristaRepository;

    public List<Motorista> findAll() {
        return motoristaRepository.findAll();
    }

    public Motorista findById(UUID id) {
        return motoristaRepository.findById(id).orElseThrow(() -> new NotFoundException("Motorista não encontrado"));
    }

    public Motorista save(SalvarMotoristaDto motorista) {
        return motoristaRepository.save(motorista.toModel());
    }
}
