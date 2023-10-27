package com.nayaragaspar.msveiculo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nayaragaspar.msveiculo.exception.CustomBadRequestException;
import com.nayaragaspar.msveiculo.exception.NotFoundException;
import com.nayaragaspar.msveiculo.model.Motorista;
import com.nayaragaspar.msveiculo.model.Veiculo;
import com.nayaragaspar.msveiculo.model.dto.SalvarVeiculoDto;
import com.nayaragaspar.msveiculo.producer.VeiculoProducer;
import com.nayaragaspar.msveiculo.repository.VeiculoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VeiculoService {
    private final VeiculoRepository veiculoRepository;
    private final VeiculoProducer veiculoProducer;
    private final MotoristaService motoristaService;

    public List<Veiculo> findAll() {
        return veiculoRepository.findAll();
    }

    public Veiculo findById(UUID id) {
        return veiculoRepository.findById(id).orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
    }

    public Veiculo findByEpcTag(String epcTag) {
        List<Veiculo> veiculos = veiculoRepository.findByEpcTag(epcTag);
        if (veiculos.isEmpty()) {
            throw new NotFoundException("Veículo não encontrado");
        }

        return veiculos.get(0);
    }

    public Veiculo save(SalvarVeiculoDto veiculo) {
        try {
            Motorista motorista = motoristaService.findById(veiculo.idMotorista());

            Veiculo result = veiculoRepository.save(veiculo.toModel(motorista));

            // gerar evento de gravação de veiculo
            veiculoProducer.publishNovoVeiculoMessage(result);

            return result;
        } catch (DataIntegrityViolationException e) {
            throw new CustomBadRequestException("Veiculo já cadastrado!", veiculo);
        } catch (NotFoundException e) {
            throw e;
        }

    }
}
