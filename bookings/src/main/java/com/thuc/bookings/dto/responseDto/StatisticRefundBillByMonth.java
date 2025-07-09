package com.thuc.bookings.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class StatisticRefundBillByMonth implements Serializable {
    private int day;
    private int amount;
}
