package com.hubspot.hubspot.services;

import org.springframework.stereotype.Service;

import com.hubspot.hubspot.client.HubSpotAuthClient;
import com.hubspot.hubspot.dto.AcessTokenResponseDto;
import com.hubspot.hubspot.exceptions.AuthenticationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RedisService redisService;
    private final HubSpotAuthClient hubSpotClient;

    public String getUrlAuthorization() {
        return hubSpotClient.formatUrlAuthorization();
    }

    public AcessTokenResponseDto getAccessToken(String code) {

        AcessTokenResponseDto tokenResponse = hubSpotClient.getToken(code);
        if (tokenResponse == null) {
            throw new AuthenticationException("Failed to retrieve access token from HubSpot API");
        }

        saveTokenToRedis(tokenResponse); 
        return tokenResponse;
    }

    

    private void saveTokenToRedis(AcessTokenResponseDto tokenResponse) {
        redisService.saveToken(tokenResponse.getAccessToken(), tokenResponse);
    }
}