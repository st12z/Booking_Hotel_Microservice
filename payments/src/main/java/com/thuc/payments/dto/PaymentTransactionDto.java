package com.thuc.payments.dto;

import com.thuc.payments.utils.TransactionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class PaymentTransactionDto {
    private Integer id;

    private String vnpTxnRef;

    private int vnpAmount;

    private String vnpTransactionNo;

    private String vnpTransactionDate;

    private String vnpResponseCode;

    private String transactionType;

    private LocalDateTime createdAt;
}
