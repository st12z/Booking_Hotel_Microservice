package com.thuc.rooms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class DiscountHotelRequestDto {
    private String code;

    private String discountType;

    private Integer discountValue;

    private String startDate;

    private String endDate;

    private Integer quantity;
}
