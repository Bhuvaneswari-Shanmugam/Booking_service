package com.bus.app.service;

import com.bus.app.dto.BusDTO;
import com.bus.app.dto.PassengerDetailDTO;
import com.bus.app.dto.PassengerRequestDTO;
import com.bus.app.dto.ResponseDTO;
import com.bus.app.entity.PassengerDetail;
import com.bus.app.repository.PassengerDetailRepository;
import com.bus.app.util.Constants;
import com.bus.auth.exception.BadRequestServiceAlertException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PassengerDetailService {

    private final PassengerDetailRepository passengerDetailsRepository;
    private final CustomerServiceWebClient customerServiceWebClient;
    private final BusServiceWebClient busServiceWebClient;

    @Transactional
    public ResponseDTO savePassengers(final String userId, final PassengerRequestDTO passengerRequestDTO) {
        for (PassengerDetailDTO passenger : passengerRequestDTO.getPassengers()) {
            final PassengerDetail obj = PassengerDetail.builder()
                    .firstName(passenger.getFirstName())
                    .lastName(passenger.getLastName())
                    .age(passenger.getAge())
                    .gender(passenger.getGender())
                    .email(passengerRequestDTO.getEmail())
                    .phoneNumber(passengerRequestDTO.getPhoneNumber())
                    .seatNumber(passenger.getSeatNumber())
                    .userId(userId)
                    .ticketId(passengerRequestDTO.getTicketId())
                    .busNumber(passengerRequestDTO.getBusNumber())
                    .updatedBy(userId)
                    .createdBy(userId)
                    .build();
            this.passengerDetailsRepository.save(obj);
        }
        return ResponseDTO.builder()
                .message(Constants.CREATED)
                .statusCode(200)
                .build();
    }

    public ResponseDTO retrieveUser(final String userId) {
        final List<PassengerDetail> passengers = this.passengerDetailsRepository.findByUserId(userId);
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .data(passengers)
                .statusCode(200)
                .build();
    }

    public ResponseDTO retrieveAllPassenger() {
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .data(this.passengerDetailsRepository.findAll())
                .statusCode(200)
                .build();
    }

    public ResponseDTO retrieveGender(final Long busNumber) {
        final List<PassengerDetail> passengers = this.passengerDetailsRepository.findByBusNumber(busNumber);

        final List<Integer> femaleSeatList = new ArrayList<>();
        final List<Integer> maleSeatNumberList = new ArrayList<>();
        final List<Integer> availableSeatNumberList = new ArrayList<>();

        final Long busCapacity = this.busServiceWebClient.retrieveBusCapacity(busNumber);
        final Long totalSeats = busCapacity;
        final Set<Integer> occupiedSeats = new HashSet<>();
        for (PassengerDetail passenger : passengers) {
            occupiedSeats.add(Math.toIntExact(passenger.getSeatNumber()));
            if ("female".equalsIgnoreCase(passenger.getGender())) {
                femaleSeatList.add(Math.toIntExact(passenger.getSeatNumber()));
            } else if ("male".equalsIgnoreCase(passenger.getGender())) {
                maleSeatNumberList.add(Math.toIntExact(passenger.getSeatNumber()));
            }
        }
        for (int i = 1; i <= totalSeats; i++) {
            if (!occupiedSeats.contains(i)) {
                availableSeatNumberList.add(i);
            }
        }
       final Map<String, Object> map = new HashMap<>();
        map.put("femaleSeatList", femaleSeatList);
        map.put("maleSeatList", maleSeatNumberList);
        map.put("availableSeatList", availableSeatNumberList);

        if(passengers.isEmpty()){
            return new ResponseDTO(Constants.RETRIEVED, map ,200);
        }
        return ResponseDTO.builder()
                .message(Constants.RETRIEVED)
                .data(map)
                .statusCode(200)
                .build();
    }
}