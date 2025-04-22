package com.thuc.bookings.entity;

import com.thuc.bookings.utils.CarType;
import com.thuc.bookings.utils.DriverStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drivers")
public class Drivers {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phoneNumber;

    private DriverStatus status;

    @OneToOne(mappedBy = "driver")
    private Vehicles vehicle;

}
