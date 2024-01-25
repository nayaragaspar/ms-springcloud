package com.nayaragaspar.gprfid.model.message;

import com.nayaragaspar.gprfid.model.PontoControle;
import com.nayaragaspar.gprfid.model.enums.TipoAntenaEnum;

public record AntenaMessage(String dsIpAntena, TipoAntenaEnum tpAntena, boolean conectar,
        String statusAntena, PontoControle pontoControle) {

}
