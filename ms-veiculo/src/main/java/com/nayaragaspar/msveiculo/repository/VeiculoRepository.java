package com.nayaragaspar.msveiculo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayaragaspar.msveiculo.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {

    List<Veiculo> findByEpcTag(String epcTag);

}
