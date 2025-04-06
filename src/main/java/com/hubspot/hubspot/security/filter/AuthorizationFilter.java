package com.hubspot.hubspot.security.filter;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.hubspot.hubspot.services.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    private final RedisService  redisService;

    public AuthorizationFilter(RedisService redisService) {
        this.redisService = redisService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/auth") || path.startsWith("/webhook") || path.startsWith("/testando") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7); 

        
        Boolean tokenIsValid = redisService.hasKey(token);

        if (!Boolean.TRUE.equals(tokenIsValid)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        
        filterChain.doFilter(request, response);
    }
}
