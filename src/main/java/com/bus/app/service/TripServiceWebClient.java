package com.bus.app.service;

import com.bus.app.dto.TripDTO;
import com.bus.app.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TripServiceWebClient {

    private final WebClient webClient;

    public TripServiceWebClient(WebClient webClientFacilities) {
        this.webClient = webClientFacilities;
    }

    public TripDTO fetchTripId(final String pickupPoint, final String destinationPoint, final String pickupTime) {
        try {
            final TripDTO tripDTO = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trip/find-trip")
                            .queryParam("pickupPoint", pickupPoint)
                            .queryParam("destinationPoint", destinationPoint)
                            .queryParam("pickupTime", pickupTime)
                            .build())
                    .retrieve()
                    .bodyToMono(TripDTO.class)
                    .block();
            if (tripDTO == null || tripDTO.getId() == null) {
                throw new IllegalArgumentException(Constants.NOT_FOUND);
            }
            return tripDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching Trip details", e);
        }
    }

    public TripDTO retrieveTrip(final String tripId) {
        try {
            final TripDTO tripDTO = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trip/retrieve-trip/{tripId}")
                            .build(tripId))
                    .retrieve()
                    .bodyToMono(TripDTO.class)
                    .block();

            if (tripDTO == null || tripDTO.getId() == null) {
                System.err.println("TripDTO is null or missing ID!");
                throw new IllegalArgumentException(Constants.NOT_FOUND);
            }

            return tripDTO;
        } catch (Exception e) {
            System.err.println("Trip catch error: " + e.getMessage());
            throw new RuntimeException("Error fetching Trip details", e);
        }
    }


}
