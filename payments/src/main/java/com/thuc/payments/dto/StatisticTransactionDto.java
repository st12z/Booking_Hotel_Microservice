package com.thuc.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticTransactionDto {
    private int day;
    private int amount;
}
