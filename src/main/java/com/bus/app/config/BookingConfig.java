package com.bus.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BookingConfig {

    @Bean
    public WebClient webClientAuth() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/auth")
                .build();
    }

    @Bean
    public WebClient webClientFacilities() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
}
