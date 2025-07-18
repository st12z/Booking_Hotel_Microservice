package com.thuc.rooms.dto;

import lombok.Data;

@Data
public class DiscountCarRequestDto {
    private String code;

    private Integer discountValue;

    private String startDate;

    private String endDate;

    private Integer quantity;

    private String description;
}
