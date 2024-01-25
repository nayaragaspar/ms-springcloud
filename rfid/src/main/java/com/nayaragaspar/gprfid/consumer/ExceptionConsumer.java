package com.nayaragaspar.gprfid.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.nayaragaspar.gprfid.model.Log;
import com.nayaragaspar.gprfid.model.enums.TipoLogEnum;
import com.nayaragaspar.gprfid.model.enums.TipoOperacaoEnum;
import com.nayaragaspar.gprfid.model.message.ReaderExceptionMessage;
import com.nayaragaspar.gprfid.repository.AntenaCacheRepository;
import com.nayaragaspar.gprfid.service.AntenaService;
import com.nayaragaspar.gprfid.service.LogService;
import com.nayaragaspar.gprfid.utility.Utility;

@Component
public class ExceptionConsumer {
    private final LogService logService;
    private final AntenaService antenaService;
    private final AntenaCacheRepository antenaCacheRepository;

    @Value("${spring.datasource.username}")
    private String usuario;

    public ExceptionConsumer(LogService logService, AntenaService antenaService,
            AntenaCacheRepository antenaCacheRepository) {
        this.logService = logService;
        this.antenaService = antenaService;
        this.antenaCacheRepository = antenaCacheRepository;
    }

    @RabbitListener(queues = "${broker.queue.reader-exception}")
    public void handleReaderException(@Payload ReaderExceptionMessage payload) {
        System.out.println("Exception consumer: " + payload.toString());

        String uri = Utility.getIpWithTcp(payload.readerUri());
        antenaCacheRepository.changeStatus(uri, "off");

        Log log = new Log(null, payload.readerUri(), TipoOperacaoEnum.ERRO, TipoLogEnum.CONEXAO_ANTENA,
                payload.errorMessage() + " | " + payload.localizedMessage(), usuario.toUpperCase(),
                usuario.toUpperCase());

        logService.save(log);

        try {
            antenaService.reconnect(uri);

            antenaCacheRepository.changeStatus(uri, "on");

            System.out.println("Antena reconectada automaticamente: " + uri);

            Log logInfo = new Log(null, payload.readerUri(), TipoOperacaoEnum.INFO, TipoLogEnum.CONEXAO_ANTENA,
                    "Antena reconectada automaticamente.", usuario.toUpperCase(), usuario.toUpperCase());

            logService.save(logInfo);
        } catch (Exception e) {
            System.err.println("Erro ao reconectar antena: " + uri + " | " + e.getMessage());
        }

    }
}
