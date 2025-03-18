package com.bus.app.service;


import com.bus.app.exception.ServiceRequestException;
import com.bus.app.util.Constants;
import com.bus.auth.exception.BadRequestServiceAlertException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CustomerServiceWebClient {

    private final WebClient webClientAuth;

    public CustomerServiceWebClient(WebClient webClientAuth) {
        this.webClientAuth = webClientAuth;
    }

    public String fetchCustomerIdByUserId(final String userId) {
        System.err.println(userId);
        try {
            boolean isUserPresent = webClientAuth.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/user-exist/{userId}")
                            .build(userId))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .blockOptional()
                    .orElse(false);
            if (!isUserPresent) {
                throw new BadRequestServiceAlertException(Constants.USER_NOT_FOUND);
            }
            return userId;
        } catch (WebClientResponseException e) {
            throw new RuntimeException(Constants.FETCH_ERROR + ": " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new ServiceRequestException(Constants.FETCH_ERROR);
        }
    }

}
