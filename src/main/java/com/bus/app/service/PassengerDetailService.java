package com.bus.app.service;

import com.bus.app.dto.PassengerDetailDTO;
import com.bus.app.dto.ResponseDTO;
import com.bus.app.entity.PassengerDetail;
import com.bus.app.repository.PassengerDetailRepository;
import com.bus.app.util.Constants;
import io.netty.util.Constant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerDetailService {

    private final PassengerDetailRepository passengerDetailsRepository;
    private final CustomerServiceWebClient customerServiceWebClient;

    public PassengerDetailService(PassengerDetailRepository passengerDetailsRepository, CustomerServiceWebClient customerServiceWebClient) {
        this.passengerDetailsRepository = passengerDetailsRepository;
        this.customerServiceWebClient = customerServiceWebClient;
    }

    public ResponseDTO registerPassengers(final String userId,final  List<PassengerDetailDTO> passengers, final String email,final  Long phoneNumber) {
        for (PassengerDetailDTO passenger : passengers) {
            final PassengerDetail obj = PassengerDetail.builder()
                    .firstName(passenger.getFirstName())
                    .lastName(passenger.getLastName())
                    .age(passenger.getAge())
                    .gender(passenger.getGender())
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .userId(userId)
                    .build();
            this.passengerDetailsRepository.save(obj);
        }
        return ResponseDTO.builder()
                .message(Constants.CREATED)
                .statusCode(200)
                .build();
    }

    public ResponseDTO fetchUser(final String userId) {
        return this.customerServiceWebClient.retrieveUserDetailsById(userId);
    }

    public ResponseDTO retrieveAllPassenger(){
                return ResponseDTO.builder()
                        .message(Constants.RETRIEVED)
                        .data(this.passengerDetailsRepository.findAll())
                        .statusCode(200)
                        .build();
    }
}