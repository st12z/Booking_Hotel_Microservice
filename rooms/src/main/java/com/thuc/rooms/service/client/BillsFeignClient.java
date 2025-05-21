package com.thuc.rooms.service.client;

import com.thuc.rooms.dto.SuccessResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "bookings",contextId = "billsClient",path = "/api/bills")
public interface BillsFeignClient {
    @GetMapping("/amount-bills-property")
    public ResponseEntity<SuccessResponseDto<Map<Integer,Integer>>> getBillByPropertyIds(@RequestParam List<Integer> propertyIds) ;
    @GetMapping("/amount-revenue-property")
    public ResponseEntity<SuccessResponseDto<Map<Integer,Integer>>> getRevenueByPropertyIds(@RequestParam List<Integer> propertyIds);
}
