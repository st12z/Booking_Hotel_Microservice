package com.thuc.bookings.controller;

import com.thuc.bookings.constants.PaymentConstant;
import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentControllers {
    private final IPaymentService paymentService;
    private final Logger log = LoggerFactory.getLogger(PaymentControllers.class);

    @PostMapping("/get-url")
    public ResponseEntity<?> getUrl(HttpServletRequest request,@RequestBody BookingDto bookingDto) {
        log.debug("Request to get payment url : {}", bookingDto);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(PaymentConstant.STATUS_200)
                .message(PaymentConstant.MESSAGE_200)
                .data(paymentService.getUrlPayment(request,bookingDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<?> getVnPayCallback(HttpServletRequest request) {
        log.debug("Request to get payment callback");
        String status = request.getParameter("vnp_ResponseCode");
        HttpStatus httpStatus  = status.equals("00") ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = status.equals("00") ? PaymentConstant.PAYMENT_SUCCESS: PaymentConstant.PAYMENT_FAILURE;
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(httpStatus.value())
                .message(message)
                .data(message)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }
}
