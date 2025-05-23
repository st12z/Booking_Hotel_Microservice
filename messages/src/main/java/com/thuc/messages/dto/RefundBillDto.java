package com.thuc.messages.dto;




public record RefundBillDto(
        String vnp_ResponseId,
        String vnp_Command,
        String vnp_ResponseCode,
        String vnp_Message,
        String vnp_TmnCode,
        String vnp_TxnRef,
        int vnp_Amount,
        String vnp_OrderInfo,
        String vnp_BankCode,
        String vnp_PayDate,
        String vnp_TransactionNo,
        String vnp_TransactionType,
        String vnp_TransactionStatus,
        String vnp_SecureHash,
        String email
) {}
