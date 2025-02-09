package com.bus.app.repository;

import com.bus.app.entity.PassengerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerDetailRepository extends JpaRepository<PassengerDetail, String> {
    List<PassengerDetail> findByUserId(String userId);
}
