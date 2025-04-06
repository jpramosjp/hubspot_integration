package com.hubspot.hubspot.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "HubSpot Integration API",
        version = "1.0",
        description = "Documentação da API de integração com HubSpot",
        contact = @Contact(name = "João Pedro Ramos", email = "jpramosjp@hotmail.com")
    )
)
public class SwaggerConfig {
    
}
