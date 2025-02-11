package com.bus.app.controller;

import com.bus.app.dto.BookingDTO;
import com.bus.app.dto.ResponseDTO;
import com.bus.app.service.BookingService;
import com.bus.app.util.JwtUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;
    private final JwtUtil jwtUtil;

    public BookingController(BookingService bookingService, JwtUtil jwtUtil) {
        this.bookingService = bookingService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseDTO bookTicket(@RequestHeader("Authorization") final String authorizationHeader,
                                  @RequestBody final BookingDTO bookingDTO) {
        final String token = authorizationHeader.substring(7);
        final String userId = jwtUtil.extractUserId(token);
        System.err.println("after extract userId in create booking  userId  :" + userId);
        return this.bookingService.bookTicket(userId, bookingDTO.getPickupPoint(), bookingDTO.getDestinationPoint(), String.valueOf(bookingDTO.getPickupTime()), bookingDTO.getBusNumber(), bookingDTO.getBusType(), bookingDTO.getBookedSeats(), bookingDTO.getPerSeatAmount(), bookingDTO.getTotalAmount());
    }

    @DeleteMapping("/cancel")
    public ResponseDTO cancelTicket(@RequestHeader("Authorization") final String authorizationHeader,
                                    @RequestParam final Long busNumber,
                                    @RequestParam final List<Long> seatNumbers) {
        final String token = authorizationHeader.substring(7);
        return this.bookingService.cancelTicket(token, busNumber, seatNumbers);
    }

    //get bookings by usedId
    @GetMapping("/retrieve/by-user")
    public List<Map<String, Object>> getUserBookings(@RequestHeader("Authorization") final String authorizationHeader,
                                                     @RequestParam final String userId) {

        return this.bookingService.getUserBookings(userId);
    }

    @PutMapping("/edit")
    public ResponseDTO modifyBooking(@RequestHeader("Authorization") final String authorizationHeader,
                                     @RequestParam final String pickupPoint,
                                     @RequestParam final String destinationPoint,
                                     @RequestParam final String pickupTime,
                                     @RequestParam final Long busNumber,
                                     @RequestParam final String busType,
                                     @RequestParam final List<Long> bookedSeats,
                                     @RequestParam final Long perSeatAmount,
                                     @RequestParam final Long totalAmount) {

        return this.bookingService.modifyBooking(authorizationHeader, pickupPoint, destinationPoint, pickupTime, busNumber, busType, bookedSeats, perSeatAmount, totalAmount);
    }

    @GetMapping("/retrieve")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDTO retrieveAllBooking(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size) {
        return this.bookingService.retrieveAllBooking(page, size);
    }


}
