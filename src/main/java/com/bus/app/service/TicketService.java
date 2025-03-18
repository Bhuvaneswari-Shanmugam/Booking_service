package com.bus.app.service;

import com.bus.app.dto.ResponseDTO;
import com.bus.app.entity.Booking;
import com.bus.app.entity.Ticket;
import com.bus.app.exception.BadRequestServiceAlertException;
import com.bus.app.repository.BookingRepository;
import com.bus.app.repository.TicketRepository;
import com.bus.app.util.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public ResponseDTO saveTicket(final String userId, final String ticketId, final String ticketUrl) {
        final List<Booking> ticketExist = this.bookingRepository.findByUserIdAndTicketId(userId, ticketId);
        final List<Booking> existBookings = this.bookingRepository.findByUserId(userId);
        if (existBookings != null && ticketExist!= null) {
            final Ticket ticket = Ticket.builder()
                    .userId(userId)
                    .ticketId(ticketId)
                    .ticketUrl(ticketUrl)
                    .createdBy(userId)
                    .createdBy(userId)
                    .build();
            return ResponseDTO.builder()
                    .message(Constants.CREATED)
                    .data(this.ticketRepository.save(ticket))
                    .statusCode(200)
                    .build();
        }
        return ResponseDTO.builder()
                .message(Constants.USER_NOT_FOUND)
                .statusCode(400)
                .build();

    }

    public ResponseDTO retrieveTicket(final String ticketId) {
        final List<Booking> bookings = this.bookingRepository.findByTicketId(ticketId);
        if (bookings.isEmpty()) {
            throw new BadRequestServiceAlertException(Constants.NOT_FOUND);
        }
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .statusCode(200)
                .data(bookings)
                .build();
    }

    public ResponseDTO retrieveAllTicket() {
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .data(this.ticketRepository.findAll())
                .statusCode(200)
                .build();
    }
}
