package com.bus.app.service;

import com.bus.app.dto.ResponseDTO;
import com.bus.app.entity.Booking;
import com.bus.app.entity.PassengerDetail;
import com.bus.app.entity.Ticket;
import com.bus.app.repository.BookingRepository;
import com.bus.app.repository.PassengerDetailRepository;
import com.bus.app.repository.TicketRepository;
import com.bus.app.util.Constants;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final PassengerDetailRepository passengerDetailsRepository;

    public TicketService(TicketRepository ticketRepository, PassengerDetailRepository passengerDetailsRepository, BookingRepository bookingRepository) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
        this.passengerDetailsRepository = passengerDetailsRepository;
    }

    public ResponseDTO createTicket(final String userId, final String ticketId, final String ticketUrl) {
        final List<Booking> existBookings = this.bookingRepository.findByUserId(userId);
        if (existBookings != null) {
            final Ticket ticket = Ticket.builder()
                    .userId(userId)
                    .ticketId(ticketId)
                    .ticketUrl(ticketUrl)
                    .createdBy(userId)
                    .updatedBy(userId)
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

    public ResponseDTO retrieveAllUserTicket(String userId, String ticketId) {
        final Optional<Ticket> ticket = this.ticketRepository.findByTicketId(ticketId);
        if (ticket.isPresent()) {
            final List<Booking> bookings = this.bookingRepository.findByUserId(userId);
            if (bookings != null) {
                final List<PassengerDetail> passengers = this.passengerDetailsRepository.findByUserId(userId);
                return ResponseDTO.builder()
                        .message(Constants.RETRIEVED)
                        .data(passengers)
                        .statusCode(200)
                        .build();
            }
        }
        return ResponseDTO.builder()
                .message(Constants.NOT_FOUND)
                .statusCode(400)
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
