package com.hubspot.hubspot.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.hubspot.dto.AcessTokenResponseDto;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
    
    private final RedisTemplate<String, String> redisTemplate;
     private final ObjectMapper objectMapper;
     
    public void saveToken(String key, AcessTokenResponseDto token) {
        try {
            String json = objectMapper.writeValueAsString(token);
            Duration duration = Duration.ofSeconds(token.getExpiresIn());
            redisTemplate.opsForValue().set(key, json, duration);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public AcessTokenResponseDto getValue(String key) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if(json == null) {
                return null;
            }
            return objectMapper.readValue(json, AcessTokenResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
