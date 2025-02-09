package com.bus.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "bus_number", nullable = false)
    private Long busNumber;

    @JoinColumn(name = "trip_id", nullable = false)
    private String tripId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @CurrentTimestamp
    @Column(name = "booking_date_time", nullable = false)
    private Date bookingDateTime;

    @Column(name = "booked_seats", nullable = false)
    private List<Long> bookedSeats;

    @Column(name = "per_seat_amount", nullable = false)
    private Long perSeatAmount;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

}
