package com.bus.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

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

    @Column(name = "trip_date", nullable = false)
    private Instant tripDate;

    @Column(name = "booked_seat", nullable = false)
    private Long bookedSeat;

    @Column(name = "per_seat_amount", nullable = false)
    private Long perSeatAmount;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Column(name = "ticketId", nullable = false)
    private String ticketId;

    @Column(name = "pickup_stop" )
    private String pickupStop;

    @Column(name = "dropping_stop" )
    private String droppingStop;

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
