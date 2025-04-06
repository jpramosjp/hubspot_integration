package com.hubspot.hubspot.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact_creation_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTable;

    private Long appId;
    private Long eventId;
    private Long subscriptionId;
    private Long portalId;
    private Long occurredAt;
    private String subscriptionType;
    private Integer attemptNumber;
    private Long objectId;
    private String changeSource;
    private String changeFlag;

    private Instant receivedAt;
}
