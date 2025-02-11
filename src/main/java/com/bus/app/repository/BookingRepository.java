package com.bus.app.repository;

import com.bus.app.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findAllByUserId(String customer);

    List<Booking> findByBusNumber(Long number);

    Booking findByUserIdAndBusNumberAndBookedSeatsIn(String userId, Long number, List<Long> bookedSeats);

    List<Booking> findByUserId(String userId);
}
