package com.bus.app.service;


import com.bus.app.dto.BusDTO;
import com.bus.app.dto.ResponseDTO;
import com.bus.app.dto.TripDTO;
import com.bus.app.entity.Booking;
import com.bus.app.exception.BadRequestServiceAlertException;
import com.bus.app.repository.BookingRepository;
import com.bus.app.util.Constants;
import com.bus.app.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripServiceWebClient tripServiceClient;
    private final BusServiceWebClient busServiceWebClient;
    private final CustomerServiceWebClient customerServiceWebClient;
    private final JwtUtil jwtUtil;

    public BookingService(BookingRepository bookingRepository, JwtUtil jwtUtil, CustomerServiceWebClient customerServiceWebClient, BusServiceWebClient busServiceWebClient, TripServiceWebClient tripServiceClient) {
        this.bookingRepository = bookingRepository;
        this.tripServiceClient = tripServiceClient;
        this.busServiceWebClient = busServiceWebClient;
        this.customerServiceWebClient = customerServiceWebClient;
        this.jwtUtil = jwtUtil;
    }


    public List<Map<String, Object>> getUserBookings(final String userId) {

        final String customer = customerServiceWebClient.fetchCustomerIdByUserId(userId);
        final List<Booking> bookings = bookingRepository.findAllByUserId(customer);
        final List<Map<String, Object>> bookingDetails = new ArrayList<>();
        for (Booking booking : bookings) {
            final Map<String, Object> bookingDetail = new HashMap<>();

            bookingDetail.put("busNumber", booking.getBusNumber());
            bookingDetail.put("Trip Id", booking.getTripId());
            bookingDetail.put("bookedNoOfSeats", booking.getBookedSeats());
            bookingDetail.put("pricePerSeat", booking.getPerSeatAmount());
            bookingDetail.put("totalPrice", booking.getTotalPrice());

            bookingDetails.add(bookingDetail);
        }
        return bookingDetails;
    }

    public ResponseDTO bookTicket(final String userId, final String pickupPoint,
                                  final String destinationPoint, final String pickupTime,
                                  final Long busNumber, final String busType,
                                  final List<Long> bookedSeats, final Long perSeatAmount, final Long totalAmount) {

        final TripDTO trip = this.tripServiceClient.fetchTripId(pickupPoint, destinationPoint, pickupTime);
        final BusDTO bus = this.busServiceWebClient.fetchBusDetail(busNumber);
        final String customer = customerServiceWebClient.fetchCustomerIdByUserId(userId);

        final List<Booking> newBookings = new ArrayList<>();
        for (Long seatNumber : bookedSeats) {
            final Booking booking = Booking.builder()
                    .busNumber(bus.getNumber())
                    .tripId(trip.getId())
                    .userId(customer)
                    .perSeatAmount(perSeatAmount)
                    .bookedSeats(List.of(seatNumber))
                    .totalPrice(totalAmount)
                    .createdBy(customer)
                    .updatedBy(customer)
                    .build();
            newBookings.add(booking);
        }
        bookingRepository.saveAll(newBookings);
        return ResponseDTO.builder()
                .message(Constants.CREATED)
                .data(newBookings)
                .statusCode(200)
                .build();
    }

    public ResponseDTO cancelTicket(final String token, final Long busNumber, final List<Long> bookedSeats) {
        final String userId = jwtUtil.extractUserId(token);
        final String customer = customerServiceWebClient.fetchCustomerIdByUserId(userId);
        final BusDTO bus = busServiceWebClient.fetchBusDetail(busNumber);
        for (Long bookedNoOfSeat : bookedSeats) {
            Booking booking = bookingRepository.findByUserIdAndBusNumberAndBookedSeatsIn(userId, bus.getNumber(), bookedSeats);
            if (booking != null) {
                bookingRepository.deleteById(booking.getId());
            }
        }
        return ResponseDTO.builder()
                .message(Constants.DELETED)
                .data(null)
                .statusCode(200)
                .build();
    }

    public ResponseDTO modifyBooking(final String authorizationHeader, final String pickupPoint, final String destinationPoint, final String pickupTime, final Long busNumber, final String busType, final List<Long> bookedSeats, final Long perSeatAmount, final Long totalAmount) {

        final String token = authorizationHeader.substring(7);
        final TripDTO trip = tripServiceClient.fetchTripId(pickupPoint, destinationPoint, pickupTime);
        final BusDTO bus = busServiceWebClient.fetchBusDetail(busNumber);
        final String userId = jwtUtil.extractUserId(token);
        final String user = customerServiceWebClient.fetchCustomerIdByUserId(userId);
        final Booking booking = bookingRepository.findByUserIdAndBusNumberAndBookedSeatsIn(userId, bus.getNumber(), bookedSeats);
        if (booking == null) {
            throw new BadRequestServiceAlertException(Constants.NOT_FOUND);
        }
        booking.setBusNumber(bus.getNumber());
        booking.setTripId(trip.getId());
        booking.setBookedSeats(bookedSeats);
        booking.setPerSeatAmount(perSeatAmount);
        booking.setTotalPrice(totalAmount);

        return ResponseDTO.builder()
                .message(Constants.UPDATED)
                .data(this.bookingRepository.save(booking))
                .statusCode(200)
                .build();
    }

    public ResponseDTO retrieveAllBooking(int page, int size) {
        final Page<Booking> userPage = this.bookingRepository.findAll(PageRequest.of(page, size));
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .data(userPage.getContent())
                .statusCode(200)
                .build();
    }
}
