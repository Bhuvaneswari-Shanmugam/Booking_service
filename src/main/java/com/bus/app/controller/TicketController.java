package com.bus.app.controller;

import com.bus.app.dto.ResponseDTO;
import com.bus.app.service.TicketService;
import com.bus.app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseDTO saveTicket(@RequestParam final String ticketId,
                                  @RequestParam final String ticketUrl) {
        System.err.println(ticketUrl);
        final String userId = this.jwtUtil.retrieveUserId();
        return this.ticketService.saveTicket(userId, ticketId, ticketUrl);
    }

    @GetMapping("/retrieve/bookings")
    public ResponseDTO retrieveTicket(@RequestParam("ticketId") final String ticketId) {
        return this.ticketService.retrieveTicket(ticketId);
    }

    @GetMapping("/retrieve/ticket")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO retrieveAllTicket() {
        return this.ticketService.retrieveAllTicket();
    }

}
