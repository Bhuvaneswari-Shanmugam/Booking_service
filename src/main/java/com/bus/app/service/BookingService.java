package com.bus.app.service;


import com.bus.app.dto.*;
import com.bus.app.entity.Booking;
import com.bus.app.entity.PassengerDetail;
import com.bus.app.exception.BadRequestServiceAlertException;
import com.bus.app.repository.BookingRepository;
import com.bus.app.repository.PassengerDetailRepository;
import com.bus.app.util.Constants;
import com.bus.app.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripServiceWebClient tripServiceClient;
    private final BusServiceWebClient busServiceWebClient;
    private final CustomerServiceWebClient customerServiceWebClient;
    private final JwtUtil jwtUtil;
    private final PassengerDetailRepository passengerDetailRepository;

    public List<Map<String, Object>> getUserBookings(final String userId) {
        final String customer = this.customerServiceWebClient.fetchCustomerIdByUserId(userId);
        final List<Booking> bookings = this.bookingRepository.findAllByUserId(customer);
        final List<Map<String, Object>> bookingDetails = new ArrayList<>();
        for (Booking booking : bookings) {
            final Map<String, Object> bookingDetail = new HashMap<>();
            bookingDetail.put("busNumber", booking.getBusNumber());
            bookingDetail.put("Trip Id", booking.getTripId());
            bookingDetail.put("bookedNoOfSeats", booking.getBookedSeat());
            bookingDetail.put("pricePerSeat", booking.getPerSeatAmount());
            bookingDetail.put("totalPrice", booking.getTotalPrice());
            bookingDetails.add(bookingDetail);
        }
        return bookingDetails;
    }

    @Transactional
    public ResponseDTO saveBooking(final String userId, final BookingDTO bookingDTO) {
        System.err.println("userId from service : "+userId);
        if (bookingDTO.getBookedSeats() == null || bookingDTO.getBookedSeats().isEmpty()) {
            return ResponseDTO.builder().message(Constants.BOOKING_CANNOT_EMPTY).statusCode(400).build();
        }
        final TripDTO trip = this.tripServiceClient.fetchTripId(bookingDTO.getPickupPoint(), bookingDTO.getDestinationPoint(), bookingDTO.getPickupTime());
        final BusDTO bus = this.busServiceWebClient.fetchBusDetail(bookingDTO.getBusNumber());
        final String customer = this.customerServiceWebClient.fetchCustomerIdByUserId(userId);
        final List<Booking> newBookings = new ArrayList<>();
        for (Long seatNumber : bookingDTO.getBookedSeats()) {
            boolean seatAlreadyBooked = this.bookingRepository
                    .findByBusNumberAndBookedSeat(bookingDTO.getBusNumber(), seatNumber)
                    .isPresent();
            if (seatAlreadyBooked) {
                return ResponseDTO.builder().message(seatNumber + Constants.SEAT_ALREADY_BOOKED).statusCode(400).build();
            }

            final Booking booking = Booking.builder()
                    .busNumber(bus.getNumber())
                    .tripId(trip.getId())
                    .userId(customer)
                    .perSeatAmount(bookingDTO.getPerSeatAmount())
                    .bookedSeat(seatNumber)
                    .totalPrice(bookingDTO.getTotalAmount())
                    .ticketId(bookingDTO.getTicketId())
                    .updatedBy(userId)
                    .tripDate(trip.getPickupTime())
                    .droppingStop(bookingDTO.getDroppingStop())
                    .pickupStop(bookingDTO.getPickupStop())
                    .createdBy(userId)
                    .build();
            newBookings.add(booking);
        }
        if (!newBookings.isEmpty()) {
            this.bookingRepository.saveAll(newBookings);
        }
        return ResponseDTO.builder()
                .message(Constants.CREATED)
                .data(newBookings)
                .statusCode(200)
                .build();
    }

    public ResponseDTO deleteBooking(final String bookingId) {
        final Optional<Booking> booking = this.bookingRepository.findById(bookingId );
        if (booking.isEmpty()) {
            return ResponseDTO.builder()
                    .message(Constants.NO_BOOKING)
                    .statusCode(400)
                    .build();
        }
        this.bookingRepository.deleteById(bookingId);
        return ResponseDTO.builder()
                .message(Constants.DELETED)
                .statusCode(200)
                .build();
    }


    public ResponseDTO retrieveAllBooking(final PaginationDTO paginationDTO) {
        final Sort sort = Sort.by(Sort.Direction.fromString(paginationDTO.getDirection()), paginationDTO.getSortBy());
        final Page<Booking> userPage = this.bookingRepository.findAll(PageRequest.of(paginationDTO.getPage(), paginationDTO.getSize(), sort));
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .data(userPage.getContent())
                .statusCode(200)
                .build();
    }

    public ResponseDTO retrievePastBooking(final String userId) {
        final List<Booking> userBookings = this.bookingRepository.findByUserId(userId);
        final List<Booking> pastBookings = this.bookingRepository.findByUserIdAndTripDate(userId, Instant.now());
        if (pastBookings.isEmpty()) {
            return new ResponseDTO(Constants.NO_BOOKING, null,200);
        }
        final List<Map<String, Object>> bookingDetailsList = new ArrayList<>();
        for (Booking userBooking : pastBookings) {
            final TripDTO tripDTO = this.tripServiceClient.retrieveTrip(userBooking.getTripId());
            final BusDTO busDTO = this.busServiceWebClient.fetchBusDetail(userBooking.getBusNumber());
            final Map<String, Object> map = new HashMap<>();
            map.put("bookingId", userBooking.getId());
            map.put("pickupPoint", tripDTO.getPickupPoint());
            //map.put("");
            map.put("reachingPoint", tripDTO.getDestinationPoint());
            map.put("tripDate", tripDTO.getPickupTime());
            map.put("busNumber", busDTO.getNumber());
            map.put("seatNumber", userBooking.getBookedSeat());
            bookingDetailsList.add(map);
        }
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .data(bookingDetailsList)
                .statusCode(200)
                .build();
    }

    public ResponseDTO retrieveUpcomingBooking(final String userId) {
        final List<Booking> userBookings = this.bookingRepository.findByUserId(userId);
        final List<Booking> upcomingBookings = this.bookingRepository.findByTripDateAndUserId(Instant.now(), userId);
        if (upcomingBookings.isEmpty()) {
           return ResponseDTO.builder()
                   .statusCode(200)
                   .message(Constants.NO_BOOKING)
                   .data(upcomingBookings)
                   .build();
        }
        final List<Map<String, Object>> bookingDetailsList = new ArrayList<>();
        for (Booking userBooking : upcomingBookings) {
            final TripDTO tripDTO = this.tripServiceClient.retrieveTrip(userBooking.getTripId());
            final BusDTO busDTO = this.busServiceWebClient.fetchBusDetail(userBooking.getBusNumber());
            final Map<String, Object> map = new HashMap<>();
            map.put("bookingId", userBooking.getId());
            map.put("pickupPoint", tripDTO.getPickupPoint());
            map.put("reachingPoint", tripDTO.getDestinationPoint());
            map.put("tripDate", tripDTO.getPickupTime());
            map.put("busNumber", busDTO.getNumber());
            map.put("seatNumber", userBooking.getBookedSeat());
            bookingDetailsList.add(map);
        }
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .data(bookingDetailsList)
                .statusCode(200)
                .build();
    }

    public ResponseDTO retrieveTicket(final String ticketId) {
        final List<Booking> bookings = this.bookingRepository.findByTicketId(ticketId);
        final List<PassengerDetail> passengers = this.passengerDetailRepository.findByTicketId(ticketId);

        if (bookings.isEmpty()) {
            throw new BadRequestServiceAlertException(Constants.NO_BOOKING);
        }
        final List<Map<String, Object>> passengerList = new ArrayList<>(); // List to store multiple passengers
        String pickupPoint = "";
        String droppingPoint = "";
        String tripDate = "";

        for (Booking booking : bookings) {
            final TripDTO tripDTO = this.tripServiceClient.retrieveTrip(booking.getTripId());
            pickupPoint = tripDTO.getPickupPoint();
            droppingPoint = tripDTO.getDestinationPoint();
            tripDate = String.valueOf(tripDTO.getPickupTime());
            break;
        }
        for (PassengerDetail passenger : passengers) {
            final Map<String, Object> map = new HashMap<>();
            map.put("id", passenger.getId());
            map.put("name", passenger.getFirstName() + " " + passenger.getLastName());
            map.put("age", passenger.getAge());
            map.put("gender", passenger.getGender());
            map.put("bus number", passenger.getBusNumber());
            map.put("seat number", passenger.getSeatNumber());
            map.put("pickup point", pickupPoint);
            map.put("dropping point", droppingPoint);
            map.put("pickup Time", tripDate);

            passengerList.add(map);
        }
        return new ResponseDTO(Constants.RETRIEVED, passengerList, 200);
    }

    public ResponseDTO cancelTicket(final String passengerId) {
        final PassengerDetail passenger = this.passengerDetailRepository.findById(passengerId)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constants.USER_NOT_FOUND));
        final Optional<Booking> booking = this.bookingRepository.findByBusNumberAndBookedSeat(passenger.getBusNumber(),passenger.getSeatNumber());
        this.bookingRepository.deleteById(booking.get().getId());
        this.passengerDetailRepository.deleteById(passengerId);
        return new ResponseDTO(Constants.DELETED , null, 200);

    }
}