package com.thuc.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCarsRequestDto implements Serializable {
    private int id;
    private int priceBooking;
}
