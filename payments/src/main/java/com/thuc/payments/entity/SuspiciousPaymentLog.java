package com.thuc.payments.entity;

import com.thuc.payments.utils.SuspiciousTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suspicious_payment_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspiciousPaymentLog extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int userId;

    private int amount;

    private String ipAddress;

    private String suspiciousReason;

    @Enumerated(EnumType.STRING)
    private SuspiciousTypeEnum suspiciousType;

    private String billCode;


}
