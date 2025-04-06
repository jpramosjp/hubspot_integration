package com.hubspot.hubspot.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hubspot.hubspot.client.HubSpotContactClient;
import com.hubspot.hubspot.dto.ContactCreationEventDto;
import com.hubspot.hubspot.dto.ContactDtoResponse;
import com.hubspot.hubspot.dto.PropertiesDto;
import com.hubspot.hubspot.entity.ContactEventEntity;
import com.hubspot.hubspot.exceptions.ContactCreationException;
import com.hubspot.hubspot.utils.JsonUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final HubSpotContactClient hubSpotClient;
    private final EventPersistenceService eventPersistenceService;

    public ContactDtoResponse createContact(PropertiesDto contactDto, String accessToken) throws JsonProcessingException {
        validateContactDto(contactDto);

        String createdContact = hubSpotClient.createContact(JsonUtils.convertToJson(contactDto), accessToken);
        if (createdContact == null) {
            throw new ContactCreationException("Failed to create contact in HubSpot API");
        }

        return ContactDtoResponse.builder()
                .result(JsonUtils.convertFromJson(createdContact, PropertiesDto.class))
                .message("Contact created successfully")
                .build();
    }

    public ContactEventEntity saveEvent(ContactCreationEventDto event) {
        validateEvent(event);
        return eventPersistenceService.saveEvent(event);
    }

    private void validateContactDto(PropertiesDto contactDto) {
        if (contactDto == null || contactDto.getProperties() == null) {
            throw new IllegalArgumentException("Invalid contact data");
        }
    }

    private void validateEvent(ContactCreationEventDto event) {
        if (event == null || event.getEventId() == null || event.getOccurredAt() == null) {
            throw new IllegalArgumentException("Invalid event data");
        }
    }
}