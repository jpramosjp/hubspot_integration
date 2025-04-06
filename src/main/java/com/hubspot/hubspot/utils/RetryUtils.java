package com.hubspot.hubspot.utils;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Message;

public class RetryUtils {

    private RetryUtils() {
    
    }

    public static long getRetryCountFromMessage(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        if (!headers.containsKey("x-death")) {
            return 0;
        }

        List<Map<String, Object>> deaths = (List<Map<String, Object>>) headers.get("x-death");
        if (deaths == null || deaths.isEmpty()) {
            return 0;
        }

        Object count = deaths.get(0).get("count");
        if (count instanceof Long) {
            return (Long) count;
        }

        return 0;
    }
}