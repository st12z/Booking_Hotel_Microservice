package com.thuc.rooms.dto;

import lombok.Data;

@Data
public class FilterDiscountCarDto {
    private Integer pageNo;

    private Integer pageSize;

    private String beginDate;

    private String endDate;

    private String timeOption;

    private String sortOption;

    private String discountStatus;
}
