package com.thuc.bookings.entity;

import com.thuc.bookings.utils.CarStatus;
import com.thuc.bookings.utils.CarType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vehicles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String licensePlate;

    private String images;

    private Double latitude;

    private Double longitude;

    private int discount;

    private int price;

    private CarStatus carStatus;

    private CarType carType;
    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Drivers driver;
}
