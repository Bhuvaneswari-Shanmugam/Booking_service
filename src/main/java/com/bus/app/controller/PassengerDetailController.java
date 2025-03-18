package com.bus.app.controller;

import com.bus.app.dto.PassengerRequestDTO;
import com.bus.app.dto.ResponseDTO;
import com.bus.app.service.PassengerDetailService;
import com.bus.app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerDetailController {

    private final PassengerDetailService passengerDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseDTO savePassengers(@RequestBody final PassengerRequestDTO passengerRequestDTO) {
        final String userId = this.jwtUtil.retrieveUserId();
        return this.passengerDetailsService.savePassengers(userId, passengerRequestDTO);
    }

    @GetMapping("/retrieve/{userId}")
    public ResponseDTO retrieveUser(@PathVariable final String userId) {
        return this.passengerDetailsService.retrieveUser(userId);
    }

    @GetMapping("/retrieve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO retrieveAllPassenger() {
        return this.passengerDetailsService.retrieveAllPassenger();
    }

    @GetMapping("/retrieve/gender/seat-list")
    public ResponseDTO retrieveGender(@RequestParam("busNumber") final Long busNumber){
        return this.passengerDetailsService.retrieveGender(busNumber);
    }
}
