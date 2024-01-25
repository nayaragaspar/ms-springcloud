package com.nayaragaspar.gprfid.producer;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.nayaragaspar.gprfid.model.message.TagMessage;
import com.nayaragaspar.gprfid.utility.Utility;

@Component
public class TagProducer {
    final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;

    private static final String TAG_READ_ROUTING_KEY = "tag_read";

    public TagProducer(RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    public void publishTag(String epcTag, String ip) {
        TagMessage message = new TagMessage(epcTag, ip, Utility.getLocalDateTimeNow());

        rabbitTemplate.convertAndSend(directExchange.getName(), TAG_READ_ROUTING_KEY, message);
    }
}
