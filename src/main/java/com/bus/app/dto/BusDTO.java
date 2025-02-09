package com.bus.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusDTO {

    private String id;
    private Long number;
    private Long capacity;
    private String type;
    private String name;
    private Long tripNumber;
    private String departureTime;
    private String pickupPoint;
    private String duration;
    private String arrivalTime;
    private String droppingPoint;
    private Double expense;
    private Double ratings;
}
