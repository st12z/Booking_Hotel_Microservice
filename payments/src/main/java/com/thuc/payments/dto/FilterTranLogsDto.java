package com.thuc.payments.dto;

import lombok.Data;

@Data
public class FilterTranLogsDto {
    private Integer pageNo;

    private Integer pageSize;

    private String beginDate;

    private String endDate;

    private String timeOption;

    private String suspiciousTranType;

    private String sortOption;

}
