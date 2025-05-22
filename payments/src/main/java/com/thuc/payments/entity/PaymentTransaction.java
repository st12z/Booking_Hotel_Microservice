package com.thuc.payments.entity;

import com.thuc.payments.utils.TransactionType;
import jakarta.persistence.*;
import lombok.*;

@Table(name="payment_transaction")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String vnpTxnRef;

    private int vnpAmount;

    private String vnpTransactionNo;

    private String vnpTransactionDate;

    private String vnpResponseCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;
}
