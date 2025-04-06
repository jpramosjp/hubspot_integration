package com.hubspot.hubspot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hubspot.hubspot.entity.ContactEventEntity;

public interface ContactEventRepository extends JpaRepository<ContactEventEntity, Long> {
}
