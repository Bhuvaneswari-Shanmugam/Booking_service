package com.bus.app.controller;

import com.bus.app.dto.BookingDTO;
import com.bus.app.dto.PaginationDTO;
import com.bus.app.dto.ResponseDTO;
import com.bus.app.service.BookingService;
import com.bus.app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseDTO saveBooking(@RequestBody final BookingDTO bookingDTO) {
        final String userId = this.jwtUtil.retrieveUserId();
        System.err.println("userid in controller :" + userId);
        return this.bookingService.saveBooking(userId, bookingDTO);
    }

    @DeleteMapping("/cancel")
    public ResponseDTO deleteBooking(@RequestParam final String bookingId) {
        return this.bookingService.deleteBooking(bookingId);
    }

    @GetMapping("/retrieve/{userId}")
    public List<Map<String, Object>> getUserBookings(@PathVariable final String userId) {
        return this.bookingService.getUserBookings(userId);
    }

    @GetMapping("retrieve/past-booking")
    public ResponseDTO retrievePastBooking() {
        final String userId = this.jwtUtil.retrieveUserId();
        return this.bookingService.retrievePastBooking(userId);
    }

    @GetMapping("retrieve/upcoming-booking")
    public ResponseDTO retrieveUpcomingBooking() {
        final String userId = this.jwtUtil.retrieveUserId();
        return this.bookingService.retrieveUpcomingBooking(userId);
    }

    @PostMapping("/retrieve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO retrieveAllBooking(@RequestBody final PaginationDTO paginationDTO) {
        return this.bookingService.retrieveAllBooking(paginationDTO);
    }

    @GetMapping("/retrieve/ticketId")
    public ResponseDTO retrieveTicket(@RequestParam("ticketId") final String ticketId) {
        return this.bookingService.retrieveTicket(ticketId);
    }

    @DeleteMapping("/cancel-ticket")
    public ResponseDTO cancelTicket(@RequestParam("passengerId") final String passengerId){
        return this.bookingService.cancelTicket(passengerId);
    }

}
