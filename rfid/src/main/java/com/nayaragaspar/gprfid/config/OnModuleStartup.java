package com.nayaragaspar.gprfid.config;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.nayaragaspar.gprfid.model.Antena;
import com.nayaragaspar.gprfid.model.Log;
import com.nayaragaspar.gprfid.model.enums.TipoLogEnum;
import com.nayaragaspar.gprfid.model.enums.TipoOperacaoEnum;
import com.nayaragaspar.gprfid.repository.AntenaCacheRepository;
import com.nayaragaspar.gprfid.service.AntenaService;
import com.nayaragaspar.gprfid.service.LogService;

@Configuration
public class OnModuleStartup implements InitializingBean {
    private final AntenaCacheRepository antennaCacheRepository;
    private final AntenaService antenaService;
    private final LogService logService;

    @Value("${spring.application.name}")
    private String sistema;

    @Value("${spring.datasource.username}")
    private String usuario;

    public OnModuleStartup(AntenaCacheRepository antennaCacheRepository, AntenaService antenaService,
            LogService logService) {
        this.antennaCacheRepository = antennaCacheRepository;
        this.antenaService = antenaService;
        this.logService = logService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        antennaCacheRepository.deleteMaches("antenna*");

        List<Antena> antenas = antenaService.getAllToConnect();
        antenaService.multiConnection(antenas);

        Log log = new Log(null, sistema, TipoOperacaoEnum.INFO, TipoLogEnum.PARADA_SISTEMA, "Sistema iniciado",
                usuario.toUpperCase(), usuario.toUpperCase());
        logService.save(log);

        System.out.println("ANTENAS CONECTADAS.");
    }
}
