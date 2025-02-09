package com.bus.app.controller;

import com.bus.app.dto.ResponseDTO;
import com.bus.app.service.TicketService;
import com.bus.app.util.JwtUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final JwtUtil jwtUtil;

    public TicketController(TicketService ticketService, JwtUtil jwtUtil) {
        this.ticketService = ticketService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/create")
    public ResponseDTO createTicket(@RequestParam final String userId,
                                    @RequestParam final String ticketId,
                                    @RequestParam final String ticketUrl) {
        return this.ticketService.createTicket(userId, ticketId, ticketUrl);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieveAllUserTicket(@RequestHeader("Authorization") final String authorizationHeader,
                                             @RequestParam final String ticketId) {
        String token = authorizationHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);
        return this.ticketService.retrieveAllUserTicket(userId, ticketId);
    }

    @GetMapping("/retrieve/all-ticket")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDTO retrieveAllTicket(@RequestHeader("Authorization") final String authorizationHeader) {
        return this.ticketService.retrieveAllTicket();
    }
}
