package com.thuc.payments.dto;

import lombok.Data;

@Data
public class FilterTransactionDto {
    private Integer pageNo;

    private Integer pageSize;

    private Integer propertyId;

    private String beginDate;

    private String endDate;

    private String timeOption;

    private String transactionType;

    private String sortOption;

    private String transactionStatus;
}
