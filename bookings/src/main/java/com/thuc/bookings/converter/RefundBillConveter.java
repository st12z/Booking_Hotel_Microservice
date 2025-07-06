package com.thuc.bookings.converter;

import com.thuc.bookings.dto.responseDto.RefundBillDto;
import com.thuc.bookings.entity.RefundBill;

public class RefundBillConveter {
    public static RefundBill toRefundBill(RefundBillDto refundBillDto) {
        return RefundBill.builder()
                .vnp_BankCode(refundBillDto.getVnp_BankCode())
                .vnp_Command(refundBillDto.getVnp_Command())
                .vnp_Message(refundBillDto.getVnp_Message())
                .vnp_OrderInfo(refundBillDto.getVnp_OrderInfo())
                .vnp_PayDate(refundBillDto.getVnp_PayDate())
                .vnp_ResponseCode(refundBillDto.getVnp_ResponseCode())
                .vnp_ResponseId(refundBillDto.getVnp_ResponseId())
                .vnp_SecureHash(refundBillDto.getVnp_SecureHash())
                .vnp_TmnCode(refundBillDto.getVnp_TmnCode())
                .vnp_TxnRef(refundBillDto.getVnp_TxnRef())
                .vnp_Amount(refundBillDto.getVnp_Amount())
                .vnp_TransactionStatus(refundBillDto.getVnp_TransactionStatus())
                .vnp_TransactionType(refundBillDto.getVnp_TransactionType())
                .vnp_TransactionNo(refundBillDto.getVnp_TransactionNo())
                .email(refundBillDto.getEmail())
                .build();
    }
    public static RefundBillDto toRefundBillDto(RefundBill refundBill) {
        return RefundBillDto.builder()
                .id(refundBill.getId())
                .vnp_TxnRef(refundBill.getVnp_TxnRef())
                .vnp_Amount(refundBill.getVnp_Amount())
                .email(refundBill.getEmail())
                .createdAt(refundBill.getCreatedAt())
                .vnp_TransactionType(refundBill.getVnp_TransactionType())
                .vnp_TransactionNo(refundBill.getVnp_TransactionNo())
                .vnp_ResponseCode(refundBill.getVnp_ResponseCode())
                .vnp_ResponseId(refundBill.getVnp_ResponseId())
                .vnp_SecureHash(refundBill.getVnp_SecureHash())
                .vnp_TransactionStatus(refundBill.getVnp_TransactionStatus())
                .vnp_Command(refundBill.getVnp_Command())
                .vnp_Message(refundBill.getVnp_Message())
                .vnp_OrderInfo(refundBill.getVnp_OrderInfo())
                .vnp_PayDate(refundBill.getVnp_PayDate())
                .build();
    }
}
