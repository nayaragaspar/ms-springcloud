package com.nayaragaspar.gprfid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.nayaragaspar.gprfid.model.Log;
import com.nayaragaspar.gprfid.model.enums.TipoLogEnum;
import com.nayaragaspar.gprfid.model.enums.TipoOperacaoEnum;
import com.nayaragaspar.gprfid.service.AntenaService;
import com.nayaragaspar.gprfid.service.LogService;

@Component
class OnModuleDestroy implements ApplicationListener<ContextClosedEvent> {
    private final AntenaService antenaService;
    private final LogService logService;

    @Value("${spring.application.name}")
    private String sistema;

    @Value("${spring.datasource.username}")
    private String usuario;

    public OnModuleDestroy(AntenaService antenaService, LogService logService) {
        this.antenaService = antenaService;
        this.logService = logService;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("ENCERRANDO A APLICAÇÃO.");

        antenaService.disconnectAll();

        Log log = new Log(null, sistema, TipoOperacaoEnum.ERRO, TipoLogEnum.PARADA_SISTEMA, "Sistema parado",
                usuario.toUpperCase(), usuario.toUpperCase());
        logService.save(log);

        System.out.println("ANTENAS DESCONECTADAS.");
    }
}
