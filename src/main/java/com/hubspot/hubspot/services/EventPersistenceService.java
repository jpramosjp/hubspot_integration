package com.hubspot.hubspot.services;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hubspot.hubspot.dto.ContactCreationEventDto;
import com.hubspot.hubspot.entity.ContactEventEntity;
import com.hubspot.hubspot.repository.ContactEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventPersistenceService {

    private final ContactEventRepository repository;

    public ContactEventEntity saveEvent(ContactCreationEventDto event) {
        ContactEventEntity contactEventEntity = ContactEventEntity.builder()
                .appId(event.getAppId())
                .eventId(event.getEventId())
                .subscriptionId(event.getSubscriptionId())
                .portalId(event.getPortalId())
                .occurredAt(event.getOccurredAt())
                .subscriptionType(event.getSubscriptionType())
                .attemptNumber(event.getAttemptNumber())
                .objectId(event.getObjectId())
                .changeSource(event.getChangeSource())
                .changeFlag(event.getChangeFlag())
                .receivedAt(Instant.now())
                .build();

        return repository.save(contactEventEntity);
    }

    public List<ContactEventEntity> allEvents() {
        return repository.findAll();
    }
}