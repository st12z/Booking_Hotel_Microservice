package com.thuc.payments.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.thuc.payments.utils.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {

    private Integer id;

    private String billCode;

    private String firstName;

    private String lastName;

    private String userEmail;

    private String email;

    private String phoneNumber;

    private int propertyId;

    private String district;

    private String city;

    private String country;

    private String addressDetail;

    private int originTotalPayment;

    private int newTotalPayment;

    private int pricePromotion;

    private int discountHotel;

    private int discountCar;

    private int isBusinessTrip;

    private int isShuttleService;

    private int bookingForWho;

    private BillStatus billStatus;

    private String specialMessage;

    private int discountCarId;

    private int discountHotelId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
