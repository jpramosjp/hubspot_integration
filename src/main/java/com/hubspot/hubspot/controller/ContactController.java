package com.hubspot.hubspot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hubspot.hubspot.dto.ContactDtoResponse;
import com.hubspot.hubspot.dto.PropertiesDto;
import com.hubspot.hubspot.services.ContactService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }



    @PostMapping("/create")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<ContactDtoResponse> createContact(
            @RequestBody PropertiesDto contactDto,
            HttpServletRequest request) {
        
        try {
            ContactDtoResponse response = contactService.createContact(contactDto, request.getHeader("Authorization"));
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body(ContactDtoResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ContactDtoResponse.builder().message(e.getMessage()).build());
        }
    }
    
}
