package com.bus.app.service;


import com.bus.app.dto.CustomerDTO;
import com.bus.app.dto.ResponseDTO;
import com.bus.app.exception.ServiceRequestException;
import com.bus.app.util.Constants;
import com.bus.auth.exception.BadRequestServiceAlertException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CustomerServiceWebClient {

    private final WebClient webClientAuth;

    public CustomerServiceWebClient(WebClient webClientAuth) {
        this.webClientAuth = webClientAuth;
    }

    public String fetchCustomerIdByUserId(final String userId) {
        try {
            boolean isUserPresent = Boolean.TRUE.equals(webClientAuth.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/user-exist")
                            .queryParam("userId", userId)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());

            if (!isUserPresent) {
                throw new BadRequestServiceAlertException(Constants.USER_NOT_FOUND);
            }

            return userId;
        } catch (Exception e) {
            throw new ServiceRequestException(Constants.FETCH_ERROR);
        }
    }

    public ResponseDTO retrieveUserDetailsById(final String userId) {
        try {
            final ResponseDTO response = webClientAuth.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/find-user")
                            .queryParam("userId", userId)
                            .build())
                    .retrieve()
                    .bodyToMono(ResponseDTO.class)
                    .block();

            if (response == null || response.getData() == null) {
                throw new IllegalArgumentException("User does not exist in AuthService.");
            }

            final CustomerDTO customer = (CustomerDTO) response.getData();

            return ResponseDTO.builder()
                    .message(response.getMessage())
                    .data(customer)
                    .statusCode(response.getStatusCode())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer data from AuthService", e);
        }
    }

}
