package com.nayaragaspar.msveiculo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nayaragaspar.msveiculo.model.Motorista;
import com.nayaragaspar.msveiculo.model.dto.SalvarMotoristaDto;
import com.nayaragaspar.msveiculo.service.MotoristaService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("motorista")
@RequiredArgsConstructor
public class MotoristaController {
    private final MotoristaService motoristaService;

    @Operation(summary = "Buscar todos os motoristas")
    @GetMapping
    public ResponseEntity<List<Motorista>> findAll() {
        return ResponseEntity.ok(motoristaService.findAll());
    }

    @Operation(summary = "Buscar motorista por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Motorista> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(motoristaService.findById(id));
    }

    @Operation(summary = "Cadastrar motorista")
    @PostMapping
    public ResponseEntity<Motorista> save(@Valid @RequestBody SalvarMotoristaDto motoristaDto) {
        System.out.println(motoristaDto);
        return ResponseEntity.ok(motoristaService.save(motoristaDto));
    }
}
