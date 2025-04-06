package com.hubspot.hubspot.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class BaseHubSpotClient {

    protected final WebClient webClient;

    public BaseHubSpotClient(WebClient.Builder webClientBuilder, @Value("${hubSpot.baseUrlAcess}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }
}