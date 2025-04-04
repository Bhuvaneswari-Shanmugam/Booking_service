package com.bus.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {

    private String pickupPoint;
    private String destinationPoint;
    private String pickupTime;
    private Long busNumber;
    private String busType;
    private String bookingStatus;
    private List<Long> bookedSeats;
    private Long perSeatAmount;
    private Long totalAmount;
    private String userId;


}
