package com.thuc.bookings.dto.responseDto;


import com.thuc.bookings.dto.requestDto.BookingCarsRequestDto;
import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto implements Serializable {
    private String addressDetail;
    private int bookingForWho;
    private List<BookingCarsRequestDto> bookingCars;
    private String city;
    private String country;
    private int discountCar;
    private int discountHotel;
    private String district;

    @NotBlank(message = "Email must have value")
    @Email
    private String email;

    @NotBlank(message = "firstName is not blank")
    private String firstName;

    private int isBusinessTrip;

    private int isShuttleService;

    @NotBlank(message = "lastName is not blank")
    private String lastName;

    @NotNull(message = "newTotalPayment is not null")
    private int newTotalPayment;

    @NotNull(message = "originTotalPayment is not null")
    private int originTotalPayment;

    @NotBlank(message = "phoneNumber is not blank")
    @Pattern(regexp = "^\\d{10}",message = "phoneNumber must be 10 digits")
    private String phoneNumber;
    private int priceCar;
    private int pricePromotion;

    @NotNull(message = "propertyId is not null")
    private int propertyId;

    private List<BookingRoomTypeDto> roomTypes;

    private String specialMessage;


    @NotBlank(message = "userEmail must have value")
    @Email
    private String userEmail;

    private int discountHotelId;

    private int discountCarId;
}
