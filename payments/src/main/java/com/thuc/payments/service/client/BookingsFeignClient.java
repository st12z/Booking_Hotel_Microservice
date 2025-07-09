package com.thuc.payments.service.client;

import com.thuc.payments.dto.BillDto;
import com.thuc.payments.dto.SuccessResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bookings",path = "/api/bills")
public interface BookingsFeignClient {
    @GetMapping("{billCode}")
    public ResponseEntity<SuccessResponseDto<BillDto>> getBillByBillCode(@PathVariable String billCode);
}
