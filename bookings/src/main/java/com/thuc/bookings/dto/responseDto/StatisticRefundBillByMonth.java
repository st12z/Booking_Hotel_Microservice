package com.thuc.bookings.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticRefundBillByMonth {
    private int day;
    private int amount;
}
