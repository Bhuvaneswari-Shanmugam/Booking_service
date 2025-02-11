package com.bus.app.controller;

import com.bus.app.dto.PassengerDetailDTO;
import com.bus.app.dto.ResponseDTO;
import com.bus.app.service.PassengerDetailService;
import com.bus.app.util.JwtUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/passenger")
public class PassengerDetailController {

    private final PassengerDetailService passengerDetailsService;
    private final JwtUtil jwtUtil;

    public PassengerDetailController(PassengerDetailService passengerDetailsService, JwtUtil jwtUtil) {
        this.passengerDetailsService = passengerDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseDTO createPassenger(@RequestHeader("Authorization") final String authorizationHeader,
                                       @RequestBody final List<PassengerDetailDTO> passengers,
                                       @RequestParam final String email,
                                       @RequestParam final Long phoneNumber) {
        final String token = authorizationHeader.substring(7);
        final String userId = jwtUtil.extractUserId(token);
        return this.passengerDetailsService.createPassenger(userId, passengers, email, phoneNumber
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseDTO fetchUser(@RequestParam final String userId) {
        return this.passengerDetailsService.fetchUser(userId);
    }


    @GetMapping("/retrieve/all-passenger")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDTO retrieveAllPassenger(@RequestHeader("Authorization") final String authorizationHeader) {
        return this.passengerDetailsService.retrieveAllPassenger();
    }
}
