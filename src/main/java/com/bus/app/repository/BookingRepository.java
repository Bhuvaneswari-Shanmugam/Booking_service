package com.bus.app.repository;

import com.bus.app.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findAllByUserId(String customer);

    List<Booking> findByUserId(String userId);

    List<Booking> findByUserIdAndTicketId(String userId, String ticketId);

    Optional<Booking> findByBusNumberAndBookedSeat(@Param("busNumber") Long busNumber,
                                                   @Param("seatNumber") Long seatNumber);

    @Query("SELECT b FROM Booking b WHERE b.bookedSeat IN :seatNumbers AND b.ticketId = :ticketId AND b.userId = :userId")
    List<Booking> findByBookedSeatsAndTicketIdAndUserId(@Param("seatNumbers") List<Long> seatNumbers,
                                                        @Param("ticketId") String ticketId,
                                                        @Param("userId") String userId);

    @Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.updatedAt <= :today")
    List<Booking> findByUserIdAndUpdatedAt(@Param("userId") String userId,
                                           @Param("today") Date today);

    @Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.tripDate <= :tripDate")
    List<Booking> findByUserIdAndTripDate(String userId, Instant tripDate);

    @Query("SELECT b FROM Booking b WHERE b.tripDate > :tripDate AND b.userId = :userId ")
    List<Booking> findByTripDateAndUserId(Instant tripDate,String userId);

    List<Booking> findByTicketId(String ticketId);
}
