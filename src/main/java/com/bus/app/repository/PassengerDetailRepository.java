package com.bus.app.repository;

import com.bus.app.entity.PassengerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerDetailRepository extends JpaRepository<PassengerDetail, String> {

    List<PassengerDetail> findByUserId(String userId);

    List<PassengerDetail> findByBusNumber(Long busNumber);

    @Query("SELECT pd FROM PassengerDetail pd WHERE pd.gender = :gender")
    List<PassengerDetail> findByGender(@Param("gender") String gender);

    List<PassengerDetail> findByTicketId(String ticketId);

    ;
}
