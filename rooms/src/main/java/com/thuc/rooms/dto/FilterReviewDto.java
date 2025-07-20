package com.thuc.rooms.dto;

import lombok.Data;

@Data
public class FilterReviewDto {
    private Integer pageNo;

    private Integer pageSize;

    private String beginDate;

    private String endDate;

    private String timeOption;

    private String sortOption;

    private Integer propertyId;
}
