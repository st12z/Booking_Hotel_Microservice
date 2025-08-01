package com.thuc.bookings.dto.responseDto;

import com.thuc.bookings.utils.CarStatus;
import com.thuc.bookings.utils.CarType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto implements Serializable {
    private Integer id;

    private String licensePlate;

    private String images;

    private Double latitude;

    private Double longitude;

    private int discount;

    private int price;

    private String status;

    private String carType;

    private int quantity;

    private int star;

    private double distanceFromProperty;

    private LocalDateTime createdAt;
}
