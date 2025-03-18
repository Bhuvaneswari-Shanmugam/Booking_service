package com.bus.app.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

