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
public class Vehicles extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String licensePlate;

    @Column(columnDefinition = "TEXT")
    private String images;

    private Double latitude;

    private Double longitude;

    private int discount;

    private int price;

    private int quantity;

    private int star;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @Enumerated(EnumType.STRING)
    private CarType carType;
    @OneToOne
    @JoinColumn(name = "driver_id")
    private Drivers driver;
}
