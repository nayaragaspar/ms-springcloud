package com.nayaragaspar.gprfid.producer;

import java.util.Objects;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.nayaragaspar.gprfid.config.RmqMessagePostProcessor;
import com.nayaragaspar.gprfid.model.dto.ExceptionCacheDto;
import com.nayaragaspar.gprfid.model.message.ReaderExceptionMessage;
import com.nayaragaspar.gprfid.repository.AntenaCacheRepository;
import com.nayaragaspar.gprfid.utility.Utility;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;

@Component
public class ExceptionProducer {
    final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final AntenaCacheRepository antenaCacheRepository;

    public static final String EXCEPTION_ROUTING_KEY = "exception";

    public ExceptionProducer(RabbitTemplate rabbitTemplate, DirectExchange directExchange,
            AntenaCacheRepository antenaCacheRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.antenaCacheRepository = antenaCacheRepository;
    }

    public void publishExceptionMessage(Reader r, ReaderException re) {
        try {
            String readerUri = r.paramGet("/reader/uri").toString();

            if (ignoreException(readerUri, re.getMessage()))
                return;

            ReaderExceptionMessage exceptionDto = new ReaderExceptionMessage(Utility.getIpWithoutTcp(readerUri),
                    re.getMessage(), re.getLocalizedMessage());

            final MessagePostProcessor messagePostProcessor = new RmqMessagePostProcessor(5000);

            rabbitTemplate.convertAndSend(directExchange.getName(), EXCEPTION_ROUTING_KEY, exceptionDto,
                    messagePostProcessor);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
    }

    private boolean ignoreException(String readerUri, String errorMessage) {
        // valida se a exceção já foi gerada mais de 2 vezes em 1 minuto
        ExceptionCacheDto errorCount = antenaCacheRepository.getException(readerUri);

        if (Objects.isNull(errorCount)) {
            ExceptionCacheDto errorCacheDto = new ExceptionCacheDto(readerUri, "1", errorMessage);
            antenaCacheRepository.saveException(errorCacheDto);
        } else {
            errorCount.setCount(String.valueOf(Long.parseLong(errorCount.getCount()) + 1));

            if (Long.parseLong(errorCount.getCount()) >= 2) {
                System.out.println("Exceção ignorada: " + readerUri + " | " + errorMessage);
                return true;
            }
        }

        return false;
    }
}
