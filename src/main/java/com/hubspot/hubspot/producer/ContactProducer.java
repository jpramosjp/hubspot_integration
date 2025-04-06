package com.hubspot.hubspot.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.hubspot.hubspot.config.RabbitMQConfig;
import com.hubspot.hubspot.dto.ContactCreationEventDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendToQueue(ContactCreationEventDto contactCreationEventDto) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            contactCreationEventDto
        );
    }
}

