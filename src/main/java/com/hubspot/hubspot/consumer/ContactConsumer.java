package com.hubspot.hubspot.consumer;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.hubspot.hubspot.config.RabbitMQConfig;
import com.hubspot.hubspot.dto.ContactCreationEventDto;
import com.hubspot.hubspot.services.ContactService;
import com.hubspot.hubspot.utils.RetryUtils;


@Component
public class ContactConsumer {
    private final ContactService contactService;
    public ContactConsumer(ContactService contactService) {
        this.contactService = contactService;
    }

     @RabbitListener(queues = RabbitMQConfig.CONTACT_QUEUE)
    public void processContact(ContactCreationEventDto contactEventDto, Message message) {
        try {
            contactService.saveEvent(contactEventDto);
        } catch (Exception e) {
            long retryCount = RetryUtils.getRetryCountFromMessage(message);
            if (retryCount < 3) {
                throw e;
            }
            
        }
    }

    
}

