package com.nayaragaspar.gprfid.model.dto;

import com.nayaragaspar.gprfid.model.Antena;
import com.nayaragaspar.gprfid.model.message.AntenaMessage;
import com.nayaragaspar.gprfid.utility.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AntenaCacheDto {
    private String dsIpAntena;
    private String tpAntena;
    private String status;
    private Long idPontoControle;
    private String nmPontoControle;
    private String dsPontoControle;
    private String statusPontoControle;
    private String tpPontoControle;

    public AntenaCacheDto(AntenaMessage antenaDto, String status) {
        this.dsIpAntena = Utility.getIpWithTcp(antenaDto.dsIpAntena());
        this.tpAntena = antenaDto.tpAntena().toString();
        this.status = status;
        this.idPontoControle = antenaDto.pontoControle().getIdPontoControle();
        this.nmPontoControle = antenaDto.pontoControle().getNmPontoControle();
        this.dsPontoControle = antenaDto.pontoControle().getDsPontoControle();
        this.statusPontoControle = antenaDto.pontoControle().getInAtivo();
        this.tpPontoControle = antenaDto.pontoControle().getTpPontoControle();
    }

    public AntenaCacheDto(Antena antena, String status) {
        this.dsIpAntena = Utility.getIpWithTcp(antena.getDsIpAntena());
        this.tpAntena = antena.getTpAntena();
        this.status = status;
        this.idPontoControle = antena.getPontoControle().getIdPontoControle();
        this.nmPontoControle = antena.getPontoControle().getNmPontoControle();
        this.dsPontoControle = antena.getPontoControle().getDsPontoControle();
        this.statusPontoControle = antena.getPontoControle().getInAtivo();
        this.tpPontoControle = antena.getPontoControle().getTpPontoControle();
    }

}
