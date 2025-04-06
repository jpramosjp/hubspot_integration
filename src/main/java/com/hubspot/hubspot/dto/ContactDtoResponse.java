package com.hubspot.hubspot.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactDtoResponse {
    @JsonProperty("result")
    PropertiesDto result;
    @JsonProperty("message")
    String message;
}
