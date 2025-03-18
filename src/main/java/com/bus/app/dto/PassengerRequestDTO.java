package com.bus.app.dto;

import com.bus.app.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRequestDTO {
    private List<PassengerDetailDTO> passengers;
    private String email;
    private Long phoneNumber;
    private String  ticketId;
    private Long busNumber;
}
