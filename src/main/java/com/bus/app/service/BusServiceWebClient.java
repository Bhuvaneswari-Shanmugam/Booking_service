package com.bus.app.service;

import com.bus.app.dto.BusDTO;
import com.bus.app.exception.BadRequestServiceAlertException;
import com.bus.app.exception.ServiceRequestException;
import com.bus.app.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BusServiceWebClient {
    private final WebClient webClient;

    public BusServiceWebClient(WebClient webClientFacilities) {
        this.webClient = webClientFacilities;
    }

    public BusDTO fetchBusDetail(final Long busNumber) {
        try {
            final BusDTO busDTO = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/bus/find-bus")
                            .queryParam("busNumber", busNumber)
                            .build())
                    .retrieve()
                    .bodyToMono(BusDTO.class)
                    .block();

            if (busDTO == null || busDTO.getId() == null) {
                throw new BadRequestServiceAlertException(Constants.NOT_FOUND);
            }

            return busDTO;
        } catch (Exception e) {
            throw new ServiceRequestException(Constants.FETCH_ERROR);

        }
    }

}
