package com.thuc.bookings.service.client;

import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.responseDto.PaymentResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payments",path = "/api/payments")
public interface PaymentsFeignClient {
    @PostMapping("/get-url")
    public ResponseEntity<SuccessResponseDto<PaymentResponseDto>> getUrl(@RequestBody BookingDto bookingDto);
}
