package com.hubspot.hubspot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hubspot.hubspot.dto.ContactCreationEventDto;
import com.hubspot.hubspot.entity.ContactEventEntity;
import com.hubspot.hubspot.producer.ContactProducer;
import com.hubspot.hubspot.services.EventPersistenceService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private final ContactProducer contactProducer;
    private final EventPersistenceService eventService;

    public WebhookController(ContactProducer contactProducer, EventPersistenceService eventService ) {
        this.contactProducer = contactProducer;
        this.eventService = eventService;
    }

    @PostMapping("/")
    public ResponseEntity<String> webhook(@RequestBody List<ContactCreationEventDto> contactCreationEventDto) {
        for (ContactCreationEventDto contactCreationEvent : contactCreationEventDto) {
            contactProducer.sendToQueue(contactCreationEvent);
        }
        return ResponseEntity.ok("Webhook received successfully!");
    }

    @GetMapping("/allEvents")
    public ResponseEntity<List<ContactEventEntity>> allEvents() {
        return ResponseEntity.ok(eventService.allEvents());
    }
    


}
