package com.thuc.bookings.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking_cars")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class BookingCars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int billId;

    private int vehicleId;

    private int priceBooking;

    private String pickupLocation;

    private String dropoffLocation;

    private LocalDateTime pickupTime;

}
