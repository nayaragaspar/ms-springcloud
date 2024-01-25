package com.nayaragaspar.gprfid.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "xcxp_sgp_ponto_controle")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PontoControle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPontoControle;
    private String nmPontoControle;
    private String dsPontoControle;
    private String inAtivo;
    private String tpPontoControle;
}
