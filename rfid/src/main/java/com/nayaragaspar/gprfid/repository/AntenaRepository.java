package com.nayaragaspar.gprfid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nayaragaspar.gprfid.model.Antena;

public interface AntenaRepository extends JpaRepository<Antena, Long> {
    public List<Antena> findByDsIpAntena(String dsIpAntena);

    @Query(nativeQuery = true, value = "select id_antena, ds_string_conexao, tp_antena, p.* from xcxp_sgp_antena a "
            + "inner join xcxp_sgp_ponto_controle p on a.id_ponto_controle = p.id_ponto_controle "
            + "where in_ativo = 'A'")
    public List<Antena> findByInStatusPontoControle();
}
