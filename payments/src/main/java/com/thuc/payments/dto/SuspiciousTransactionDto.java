package com.thuc.payments.dto;

import com.thuc.payments.utils.SuspiciousTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuspiciousTransactionDto {
    private Integer id;

    private int userId;

    private int amount;

    private String ipAddress;

    private String suspiciousReason;

    private SuspiciousTypeEnum suspiciousType;

    private String billCode;
}
