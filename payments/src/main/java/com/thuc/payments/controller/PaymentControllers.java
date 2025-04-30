package com.thuc.payments.controller;

import com.thuc.payments.constant.PaymentConstant;
import com.thuc.payments.dto.BookingDto;
import com.thuc.payments.dto.PaymentResponseDto;
import com.thuc.payments.dto.SuccessResponseDto;
import com.thuc.payments.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getVnPayCallback(HttpServletRequest request) {
        log.debug("Request to get payment callback");
        String status = request.getParameter("vnp_ResponseCode");
        HttpStatus httpStatus  = status.equals("00") ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = status.equals("00") ? PaymentConstant.PAYMENT_SUCCESS: PaymentConstant.PAYMENT_FAILURE;
        SuccessResponseDto<?> response = SuccessResponseDto.builder()
                .code(httpStatus.value())
                .message(message)
                .data(message)
                .build();
        if(status.equals("00")) {
            // send message đến booking cập nhật đơn hàng
            log.debug("send payment callback :{}",request.getParameter("vnp_TxnRef"));
            var result = streamBridge.send("sendPayment-out-0",request.getParameter("vnp_TxnRef"));
            log.debug("Receive payment callback :{}",result);

        }
        return ResponseEntity.status(httpStatus).body(response);
    }
}
