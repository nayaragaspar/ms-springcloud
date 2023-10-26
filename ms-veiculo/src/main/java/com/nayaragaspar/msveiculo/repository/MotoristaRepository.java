package com.nayaragaspar.msveiculo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayaragaspar.msveiculo.model.Motorista;

public interface MotoristaRepository extends JpaRepository<Motorista, UUID> {

}
