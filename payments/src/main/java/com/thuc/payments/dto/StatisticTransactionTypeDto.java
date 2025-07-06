package com.thuc.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticTransactionTypeDto {
    private String transactionType;
    private int amount;
}
