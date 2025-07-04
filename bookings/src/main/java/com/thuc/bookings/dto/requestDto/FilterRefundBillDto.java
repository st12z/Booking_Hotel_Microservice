package com.thuc.bookings.dto.requestDto;

import lombok.Data;

@Data
public class FilterRefundBillDto {
    private Integer pageNo;

    private Integer pageSize;

    private Integer propertyId;

    private String beginDate;

    private String endDate;

    private String timeOption;

    private String sortOption;

    private String transactionType;
}
