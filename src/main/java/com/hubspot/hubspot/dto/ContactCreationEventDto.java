package com.hubspot.hubspot.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactCreationEventDto implements Serializable {

    @JsonProperty("appId")
    private Long appId;

    @JsonProperty("eventId")
    private Long eventId;

    @JsonProperty("subscriptionId")
    private Long subscriptionId;

    @JsonProperty("portalId")
    private Long portalId;

    @JsonProperty("occurredAt")
    private Long occurredAt;

    @JsonProperty("subscriptionType")
    private String subscriptionType;

    @JsonProperty("attemptNumber")
    private Integer attemptNumber;

    @JsonProperty("objectId")
    private Long objectId;

    @JsonProperty("changeSource")
    private String changeSource;

    @JsonProperty("changeFlag")
    private String changeFlag;
}
