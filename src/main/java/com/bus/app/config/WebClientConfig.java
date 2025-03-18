package com.bus.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${webclient.auth.baseurl}")
    private String authBaseUrl;

    @Value("${webclient.facility.baseurl}")
    private String facilityBaseUrl;

    @Bean
    public WebClient webClientAuth() {
        return WebClient.builder()
                .baseUrl(authBaseUrl)
                .build();
    }

    @Bean
    public WebClient webClientFacilities() {
        return WebClient.builder()
                .baseUrl(facilityBaseUrl)
                .build();
    }
}
