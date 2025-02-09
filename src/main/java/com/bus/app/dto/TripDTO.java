package com.bus.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {

    private String id;
    private String pickupPoint;
    private String destinationPoint;
    private Instant pickupTime;
    private Instant reachingTime;
    private Long expense;

}