package com.nayaragaspar.gprfid.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "xcxp_sgp_antena")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Antena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAntena;
    @ManyToOne
    @JoinColumn(name = "idPontoControle")
    private PontoControle pontoControle;
    @Column(name = "ds_string_conexao")
    private String dsIpAntena;
    private String tpAntena;
}
