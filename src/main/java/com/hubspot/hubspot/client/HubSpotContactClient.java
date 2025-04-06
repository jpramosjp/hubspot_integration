package com.hubspot.hubspot.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HubSpotContactClient extends BaseHubSpotClient {

    public HubSpotContactClient(WebClient.Builder webClientBuilder, @Value("${hubSpot.baseUrlAcess}") String baseUrl) {
        super(webClientBuilder, baseUrl);
    }

    public String createContact(String contactJson, String accessToken) {
        return webClient.post()
                .uri("/crm/v3/objects/contacts")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(contactJson)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}