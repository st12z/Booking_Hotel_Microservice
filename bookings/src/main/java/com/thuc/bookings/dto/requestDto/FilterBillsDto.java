package com.thuc.bookings.dto.requestDto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class FilterBillsDto {
    private Integer pageNo;

    private Integer pageSize;

    private Integer propertyId;

    private String beginDate;

    private String endDate;


    private String timeOption;

    private String billTypeStatus;

    private String sortOption;

}
