package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDto {
    private int id;

    private String code;

    private String discountType;

    private int discountValue;

    private int minBookingAmount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int quantity;

    private String image;
}
