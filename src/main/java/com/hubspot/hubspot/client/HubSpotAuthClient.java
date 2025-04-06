package com.hubspot.hubspot.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.hubspot.hubspot.dto.AcessTokenResponseDto;

@Component
public class HubSpotAuthClient extends BaseHubSpotClient {

    @Value("${hubSpot.baseUrlAuth}")
    private String baseUrlAuth;

    @Value("${hubSpot.scope}")
    private String scope;

    @Value("${hubSpot.clientId}")
    private String clientId;

    @Value("${hubSpot.redirectUri}")
    private String redirectUri;

    @Value("${hubSpot.state}")
    private String state;

    @Value("${hubSpot.clientSecret}")
    private String clientSecret;

    public HubSpotAuthClient(WebClient.Builder webClientBuilder, @Value("${hubSpot.baseUrlAcess}") String baseUrl) {
        super(webClientBuilder, baseUrl);
    }

    public String formatUrlAuthorization() {
        return String.format("%s/oauth/authorize?client_id=%s&scope=%s&redirect_uri=%s&state=%s",
                baseUrlAuth, clientId, scope, redirectUri, state);
    }

    public AcessTokenResponseDto getToken(String code) {
        return webClient.post()
                .uri("/oauth/v1/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=authorization_code&client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&redirect_uri=" + redirectUri +
                        "&code=" + code)
                .retrieve()
                .bodyToMono(AcessTokenResponseDto.class)
                .block();
    }
}