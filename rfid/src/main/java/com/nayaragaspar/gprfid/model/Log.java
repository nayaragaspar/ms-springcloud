package com.nayaragaspar.gprfid.model;

import java.math.BigDecimal;

import com.nayaragaspar.gprfid.model.enums.TipoLogEnum;
import com.nayaragaspar.gprfid.model.enums.TipoOperacaoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "xcxp_sgp_controle_log")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_controle_log")
    private BigDecimal id;
    private String cdIdentificador;
    @Enumerated(EnumType.STRING)
    private TipoOperacaoEnum tpOperacao;
    @Enumerated(EnumType.STRING)
    private TipoLogEnum nmOperacao;
    private String dsOperacao;
    private String nmUsuarioCriacao;
    private String nmUsuarioAlteracao;
}
