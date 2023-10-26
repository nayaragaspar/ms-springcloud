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

import com.nayaragaspar.msveiculo.model.Veiculo;
import com.nayaragaspar.msveiculo.model.dto.SalvarVeiculoDto;
import com.nayaragaspar.msveiculo.service.VeiculoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("veiculo")
@RequiredArgsConstructor
@Tag(name = "Veiculo")
public class VeiculoController {
    private final VeiculoService veiculoService;

    @Operation(summary = "Buscar todos os veículos")
    @GetMapping
    public ResponseEntity<List<Veiculo>> findAll() {
        return ResponseEntity.ok(veiculoService.findAll());
    }

    @Operation(summary = "Buscar veículo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(veiculoService.findById(id));
    }

    @Operation(summary = "Buscar veículo por ID")
    @GetMapping("/tag/{epcTag}")
    public ResponseEntity<Veiculo> findByEpcTag(@PathVariable String epcTag) {
        return ResponseEntity.ok(veiculoService.findByEpcTag(epcTag));
    }

    @Operation(summary = "Cadastrar veículo")
    @PostMapping
    public ResponseEntity<Veiculo> save(@Valid @RequestBody SalvarVeiculoDto veiculo) {
        return ResponseEntity.ok(veiculoService.save(veiculo));
    }
}
