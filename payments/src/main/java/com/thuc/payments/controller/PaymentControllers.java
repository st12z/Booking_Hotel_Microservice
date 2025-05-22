package com.thuc.payments.controller;

import com.thuc.payments.constant.PaymentConstant;
import com.thuc.payments.dto.BookingDto;
import com.thuc.payments.dto.PaymentResponseDto;
import com.thuc.payments.dto.SuccessResponseDto;
import com.thuc.payments.service.IPaymentService;
import com.thuc.payments.service.IPaymentTransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ResponseEntity<SuccessResponseDto<String>> refund(HttpServletRequest request, @PathVariable String billCode) {
        log.debug("Request to refund payment url : {}", request);
        SuccessResponseDto<String> response = SuccessResponseDto.<String>builder()
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
        if(vnpResponseCode.equals("00")) {
            // tạo payment transaction
            paymentTransactionService.createPayment(vnpResponseCode,vnpTxnRef,vnpAmount,vnpTransactionNo,vnpTransactionDate);

            // send message đến booking cập nhật đơn hàng
            log.debug("send payment callback :{}",request.getParameter("vnp_TxnRef"));
            var result = streamBridge.send("sendPayment-out-0",request.getParameter("vnp_TxnRef"));
            log.debug("Receive payment callback :{}",result);
        }
        response.sendRedirect("http://localhost:3000/payments?status=" + status + "&billCode=" + billCode);

    }

}
