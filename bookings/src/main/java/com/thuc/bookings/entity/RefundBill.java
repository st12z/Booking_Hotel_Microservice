package com.thuc.bookings.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refund_bills")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundBill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String vnp_ResponseId;
    private String vnp_Command;
    private String vnp_ResponseCode;
    private String vnp_Message;
    private String vnp_TmnCode;
    private String vnp_TxnRef;
    private int vnp_Amount;
    private String vnp_OrderInfo;
    private String vnp_BankCode;
    private String vnp_PayDate;
    private String vnp_TransactionNo;
    private String vnp_TransactionType;
    private String vnp_TransactionStatus;
    private String vnp_SecureHash;
    private String email;
}
