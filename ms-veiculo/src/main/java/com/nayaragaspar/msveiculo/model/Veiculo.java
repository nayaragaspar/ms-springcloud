package com.nayaragaspar.msveiculo.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String placa;
    @Column
    private String modelo;
    @Column
    private String marca;
    @Column
    private String epcTag;
    @Column
    private String memoryTag;
    @ManyToOne
    private Motorista motorista;
}
