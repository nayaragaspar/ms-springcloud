package com.nayaragaspar.gprfid.consumer;

import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.nayaragaspar.gprfid.model.Log;
import com.nayaragaspar.gprfid.model.dto.AntenaCacheDto;
import com.nayaragaspar.gprfid.model.enums.TipoLogEnum;
import com.nayaragaspar.gprfid.model.enums.TipoOperacaoEnum;
import com.nayaragaspar.gprfid.model.message.AntenaMessage;
import com.nayaragaspar.gprfid.repository.AntenaCacheRepository;
import com.nayaragaspar.gprfid.service.AntenaService;
import com.nayaragaspar.gprfid.service.LogService;
import com.nayaragaspar.gprfid.utility.Utility;

@Component
public class AntenaConsumer {
    private final AntenaService antenaService;
    private final AntenaCacheRepository antenaCacheRepository;
    private final LogService logService;

    @Value("${spring.datasource.username}")
    private String usuario;

    public AntenaConsumer(AntenaService antenaService, AntenaCacheRepository antenaCacheRepository,
            LogService logService) {
        this.antenaService = antenaService;
        this.antenaCacheRepository = antenaCacheRepository;
        this.logService = logService;
    }

    @RabbitListener(queues = "${broker.queue.antena}")
    public void conectarAntena(@Payload AntenaMessage payload) {
        System.out.println("conectarAntena: " + payload.toString());

        String ipAntena = Utility.getIpWithTcp(payload.dsIpAntena());

        if (payload.pontoControle().getInAtivo().equals("I") ||
                payload.statusAntena().equals("I")) {
            antenaService.remove(ipAntena);

            Log log = new Log(null, ipAntena, TipoOperacaoEnum.ERRO, TipoLogEnum.CONEXAO_ANTENA,
                    "Desconectado por alteração do ponto de controle e/ou antena!", usuario.toUpperCase(),
                    usuario.toUpperCase());
            logService.save(log);

        } else if (Boolean.TRUE.equals(payload.conectar())) {
            AntenaCacheDto antenaCacheDto = antenaCacheRepository.get(ipAntena);

            if (Objects.isNull(antenaCacheDto) || antenaCacheDto.getStatus().equals("off")) {
                antenaService.reconnect(ipAntena);
            } else {
                AntenaCacheDto antena = new AntenaCacheDto(payload, "on");
                antenaCacheRepository.save(antena);
            }

            Log log = new Log(null, ipAntena, TipoOperacaoEnum.INFO, TipoLogEnum.CONEXAO_ANTENA,
                    "Conectado por criação / alteração do ponto de controle e/ou antena!", usuario.toUpperCase(),
                    usuario.toUpperCase());
            logService.save(log);

        } else {
            AntenaCacheDto antenaCacheDto = new AntenaCacheDto(payload, "off");
            antenaCacheRepository.save(antenaCacheDto);
        }
    }

    @RabbitListener(queues = "${broker.queue.antena}" + ".dlq")
    public void conectarAntenaDlq(@Payload AntenaMessage payload) {
        System.out.println("DLQ - Não foi possível conectar/desconectar Antena: " + payload.toString());
    }
}
