package com.thuc.payments.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thuc.payments.constant.PaymentConstant;
import com.thuc.payments.dto.*;
import com.thuc.payments.service.IPaymentService;
import com.thuc.payments.service.IPaymentTransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentControllers {
    private final IPaymentService paymentService;
    private final Logger log = LoggerFactory.getLogger(PaymentControllers.class);
    private final StreamBridge streamBridge;
    private final IPaymentTransactionService paymentTransactionService;
    @PostMapping("/get-url")
    public ResponseEntity<SuccessResponseDto<PaymentResponseDto>> getUrl(HttpServletRequest request,@RequestBody BookingDto bookingDto) {
        log.debug("Request to get payment url : {}", bookingDto);
        SuccessResponseDto<PaymentResponseDto> response = new SuccessResponseDto<>(
                PaymentConstant.STATUS_200,
                PaymentConstant.MESSAGE_200,
                paymentService.getUrlPayment(request, bookingDto)
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping("/refund/{billCode}")
    public ResponseEntity<SuccessResponseDto<VnpayRefundResponseDto>> refund(HttpServletRequest request, @PathVariable String billCode) throws JsonProcessingException {
        log.debug("Request to refund payment url : {}", request);
        SuccessResponseDto<VnpayRefundResponseDto> response = SuccessResponseDto.<VnpayRefundResponseDto>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(paymentService.refund(request,billCode))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/vn-pay-callback")
    public void getVnPayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("Request to get payment callback");
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
        String vnpTxnRef = request.getParameter("vnp_TxnRef");
        int vnpAmount = Integer.parseInt(request.getParameter("vnp_Amount"));
        String vnpTransactionNo = request.getParameter("vnp_TransactionNo");
        String vnpTransactionDate = request.getParameter("vnp_PayDate");

        int status = vnpResponseCode.equals("00") ? PaymentConstant.STATUS_200 : PaymentConstant.STATUS_500;
        String billCode =request.getParameter("vnp_TxnRef");
        paymentTransactionService.createPayment(vnpResponseCode,vnpTxnRef,vnpAmount,vnpTransactionNo,vnpTransactionDate);
        if(vnpResponseCode.equals("00")) {
            // tạo payment transaction
            // send message đến booking cập nhật đơn hàng
            log.debug("send payment callback :{}",request.getParameter("vnp_TxnRef"));
            var result = streamBridge.send("sendPayment-out-0",request.getParameter("vnp_TxnRef"));
            log.debug("Receive payment callback :{}",result);
        }
        response.sendRedirect("http://localhost:3000/payments?status=" + status + "&billCode=" + billCode);

    }
    @PostMapping("amount-transaction-month")
    public ResponseEntity<SuccessResponseDto<List<StatisticTransactionDto>>> getAmountTransactionMonth(@RequestBody FilterStatistic filterDto) {
        log.debug("Request to get amount transaction filter : {}", filterDto);
        SuccessResponseDto<List<StatisticTransactionDto>> response = SuccessResponseDto.<List<StatisticTransactionDto>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(paymentService.getAmountTransactionMonth(filterDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("revenue-transaction-month")
    public ResponseEntity<SuccessResponseDto<List<StatisticTransactionDto>>> getRevenueTransactionMonth(@RequestBody FilterStatistic filterDto) {
        log.debug("Request to get amount transaction filter : {}", filterDto);
        SuccessResponseDto<List<StatisticTransactionDto>> response = SuccessResponseDto.<List<StatisticTransactionDto>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(paymentService.getRevenueTransactionByMonth(filterDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("statistic-transactiontype-month/{month}")
    public ResponseEntity<SuccessResponseDto<List<StatisticTransactionTypeDto>>> getRevenueTransactionMonth(@PathVariable Integer month) {
        log.debug("Request to get statistic transaction month : {}", month);
        SuccessResponseDto<List<StatisticTransactionTypeDto>> response = SuccessResponseDto.<List<StatisticTransactionTypeDto>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(paymentService.getStatisticTransactionType(month))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("list-transactions")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<PaymentTransactionDto>>>> getAllTransactions(@RequestBody FilterTransactionDto filterDto) throws ParseException {
        log.debug("Request to get all transactions");
        SuccessResponseDto<PageResponseDto<List<PaymentTransactionDto>>> response = SuccessResponseDto.<PageResponseDto<List<PaymentTransactionDto>>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(paymentTransactionService.getAllTransactions(filterDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("transaction-types")
    public ResponseEntity<SuccessResponseDto<List<String>>> getAllTransactionTypes() {
        log.debug("Request to get all transaction types");
        SuccessResponseDto<List<String>> response = SuccessResponseDto.<List<String>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(paymentTransactionService.getALlTransactionTypes())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<PaymentTransactionDto>>>> searchTransactions(
            @RequestParam(defaultValue = "1",required = false) Integer pageNo,
            @RequestParam(defaultValue = "10",required = false) Integer pageSize,
            @RequestParam(defaultValue = "",required = false) String keyword
    ) {
        log.debug("Request to search transactions : {}", keyword);
        SuccessResponseDto<PageResponseDto<List<PaymentTransactionDto>>> response = SuccessResponseDto.<PageResponseDto<List<PaymentTransactionDto>>>builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(paymentTransactionService.getSearchTransaction(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
