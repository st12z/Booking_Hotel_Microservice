package com.thuc.payments.controller;

import com.thuc.payments.constant.PaymentConstant;
import com.thuc.payments.dto.BookingDto;
import com.thuc.payments.dto.PaymentResponseDto;
import com.thuc.payments.dto.SuccessResponseDto;
import com.thuc.payments.service.IPaymentService;
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

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentControllers {
    private final IPaymentService paymentService;
    private final Logger log = LoggerFactory.getLogger(PaymentControllers.class);
    private final StreamBridge streamBridge;
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
    @GetMapping("/vn-pay-callback")
    public void getVnPayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("Request to get payment callback");
        String status = request.getParameter("vnp_ResponseCode");
        int statusReturn = status.equals("00") ? PaymentConstant.STATUS_200 : PaymentConstant.STATUS_500;
        String billCode =request.getParameter("vnp_TxnRef");
        if(status.equals("00")) {
            // send message đến booking cập nhật đơn hàng
            log.debug("send payment callback :{}",request.getParameter("vnp_TxnRef"));
            var result = streamBridge.send("sendPayment-out-0",request.getParameter("vnp_TxnRef"));
            log.debug("Receive payment callback :{}",result);
        }
        response.sendRedirect("http://localhost:3000/payments?status=" + statusReturn + "&billCode=" + billCode);

    }
}
