package com.thuc.bookings.dto.requestDto;

import lombok.Data;

@Data
public class FilterCarAdminDto {
    private Integer pageNo;

    private Integer pageSize;

    private String beginDate;

    private String endDate;

    private String timeOption;

    private String carType;

    private String sortOption;

    private String carStatus;
}
